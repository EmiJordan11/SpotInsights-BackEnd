package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.client.SpotifyApiClient;
import com.emijordan.Spotinsights.dto.*;
import com.emijordan.Spotinsights.entities.*;
import com.emijordan.Spotinsights.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SpotifyDataService {
    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private ArtistService artistService;

    @Autowired
    private  UserService userService;

    @Autowired
    private  GenreService genreService;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private SongService songService;

    @Autowired
    private ReproductionService reproductionService;

    //BORRAR
    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private ReproductionRepository reproductionRepository;

    public void getData(TokenResponse spotifyTokens){
        //Set accessToken
        spotifyApiClient.setAuthToken(spotifyTokens.accessToken());

        //User
        User user = userService.getUser(spotifyTokens.refreshToken());

        //Get response of Spotify API
        SpotifyApiResponse response = getApiResponse();

        Set<String> artistIds = new HashSet<>();
        Set<String> albumIds = new HashSet<>();
        Set<String> songIds = new HashSet<>();

        //almaceno las entidades existentes en los HashSet
        for (ItemDTO i: response.items()){
            i.song().artists().forEach(a-> artistIds.add(a.idSpotify()));
            //add album id
            albumIds.add(i.song().album().idSpotify());
            //add song id
            songIds.add(i.song().idSpotify());
        }

        //Almaceno las entidades existentes en un Map,donde la clave es el idSpotify y el valor es la entidad
        Map<String, Artist> existingArtists = artistService.getExistingArtists(artistIds).stream()
                .collect(Collectors.toMap(Artist::getIdSpotify, Function.identity()));

        Map<String, Album> existingAlbums = albumService.getExistingAlbums(albumIds).stream()
                .collect(Collectors.toMap(Album::getIdSpotify, Function.identity()));

        Map<String, Song> existingSongs = songService.getExistingSongs(songIds).stream()
                .collect(Collectors.toMap(Song::getIdSpotify, Function.identity()));

        //Las entidades que no existen las guardo en una Lista para posteriormente perisistirlas por lote
        List<Artist> newArtists = new ArrayList<>();
        List<Album> newAlbums = new ArrayList<>();
        List<Song> newSongs = new ArrayList<>();
        List<Reproduction> newReproductions = new ArrayList<>();


        for (ItemDTO i: response.items()){
            //Album artist
            Artist albumArtist = null;
            Artist existingAlbumArtist = existingArtists.get(i.song().album().artists().get(0).idSpotify());
            if (existingAlbumArtist!=null){
                albumArtist = existingAlbumArtist;
            }
            else{
                albumArtist = artistService.getAlbumArtist(i);
                existingArtists.put(albumArtist.getIdSpotify(), albumArtist); //lo agrego a los artistas existentes
            }

            //Album
            Album album = null;
            Album existingAlbum = existingAlbums.get(i.song().album().idSpotify());
            if (existingAlbum!=null){
                album = existingAlbum;
            }
            else{
                album = albumService.buildAlbum(i.song().album(), albumArtist);
                newAlbums.add(album);
                existingAlbums.put(album.getIdSpotify(), album); //lo agrego a los albumes existentes
            }

            //Song artists
            //defino una lista de los artistas de la cancion
            List<Artist> songArtists = new ArrayList<>();

            //guardo los artistas que no existen en una lista
            List<ArtistDTO> newArtistsDTO = new ArrayList<>();
            for (ArtistDTO artistDTO: i.song().artists()){
                Artist existingArtist = existingArtists.get(artistDTO.idSpotify());
                //si no existe, lo guardo en la lista de nuevos artistas
                if (existingArtist==null) {
                    newArtistsDTO.add(artistDTO);
                }
                //sino, lo guardo en la lista de artistas existentes de esta cancion
                else{
                    songArtists.add(existingArtists.get(artistDTO.idSpotify()));
                }
            }

            //creo los nuevos artistas y luego los agrego a la lista de artistas de la cancion
            List<Artist> buildingArtists = artistService.buildArtists(newArtistsDTO);
            buildingArtists.stream().forEach(a-> songArtists.add(a));

            //tambien los agrego a la lista de nuevos artistas y artistas existentes
            buildingArtists.stream().forEach(a-> newArtists.add(a));
            buildingArtists.stream().forEach(a-> existingArtists.put(a.getIdSpotify(), a));

            //SONG
            Song song = null;
            Song existingSong = existingSongs.get(i.song().idSpotify());
            if (existingSong!=null){
                song = existingSong;
            }
            else{
                song = songService.buildSong(i, album, songArtists);
                newSongs.add(song);
                existingSongs.put(song.getIdSpotify(), song);
            }

            //Reproduction
            Reproduction reproduction = reproductionService.buildReproduction(i, user, song);
            newReproductions.add(reproduction);

        }

        //Guardo por lotes las entidades nuevas
        artistService.saveAll(newArtists);
        albumService.saveAll(newAlbums);
        songService.saveAll(newSongs);

        newReproductions.stream().forEach(r->reproductionService.saveReproduction(r));//no necesariamente son todas nuevas, el metodo verifica si no existen con anterioridad

    }

    public SpotifyApiResponse getApiResponse(){
        String json = spotifyApiClient.getRecentlyPlayed();
        SpotifyApiResponse response = Mappers.convertData(json, SpotifyApiResponse.class);
        return response;
    }

}
