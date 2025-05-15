package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.client.SpotifyApiClient;
import com.emijordan.Spotinsights.controller.DataSyncController;
import com.emijordan.Spotinsights.dto.*;
import com.emijordan.Spotinsights.entities.*;
import com.emijordan.Spotinsights.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private AlbumService albumService;

    @Autowired
    private SongService songService;

    @Autowired
    private ReproductionService reproductionService;

    private static final Logger logger = LoggerFactory.getLogger(DataSyncController.class);

    //Obtener data post login en el front
    public void syncDataAfterLogin(TokenResponse spotifyTokens){
        //USER
        User user = userService.getUser(spotifyTokens);

        logger.info("Nuevo usuario registrado: "+user.getName());

        syncSpotifyData(user, spotifyTokens.accessToken());
    }

    //Actualizar data (tarea automatizada)
    public void scheduledSyncForExistingUsers(String accessToken, User user){
        syncSpotifyData(user, accessToken);
    }

    public SpotifyApiResponse getApiResponse(String accessToken){
        String json = spotifyApiClient.getRecentlyPlayed(accessToken);
        SpotifyApiResponse response = Mappers.convertData(json, SpotifyApiResponse.class);
        return response;
    }


    //Logica en comun para obtener los datos
    public void syncSpotifyData(User user, String accessToken){

        //Get response of Spotify API
        SpotifyApiResponse response = getApiResponse(accessToken);

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

        //----------------------------------------------------------------------------------------------------------
        for (ItemDTO i: response.items()){
            //ALBUM ARTISTS
            List<Artist> albumArtists = new ArrayList<>();

            //guardo los artistas que no existen en una lista (ya sean del album o de la cancion)
            List<ArtistDTO> newAlbumArtistsDTO = new ArrayList<>();
            for (ArtistDTO artistDTO : i.song().album().artists()){
                Artist existingAlbumArtist = existingArtists.get(artistDTO.idSpotify());
                if (existingAlbumArtist!=null){
                    albumArtists.add(existingAlbumArtist);
                }
                else{
                    newAlbumArtistsDTO.add(artistDTO);
                }
            }
            //creo los nuevos artistas y luego los agrego a la lista de artistas del album
            List<Artist> buildingAlbumArtists = artistService.buildArtists(newAlbumArtistsDTO, accessToken);
            albumArtists.addAll(buildingAlbumArtists);

            //tambien los agrego a la lista de nuevos artistas y artistas existentes
            newArtists.addAll(buildingAlbumArtists);
            buildingAlbumArtists.stream().forEach(a->existingArtists.put(a.getIdSpotify(),a));

            //----------------------------------------------------------------------------------------------------------
            //ALBUM
            Album album = null;
            Album existingAlbum = existingAlbums.get(i.song().album().idSpotify());
            if (existingAlbum!=null){
                album = existingAlbum;
            }
            else{
                album = albumService.buildAlbum(i.song().album(), albumArtists);
                newAlbums.add(album);
                existingAlbums.put(album.getIdSpotify(), album); //lo agrego a los albumes existentes
            }

            //----------------------------------------------------------------------------------------------------------
            //SONG ARTISTS
            //defino una lista de los artistas de la cancion
            List<Artist> songArtists = new ArrayList<>();
            List<ArtistDTO> newSongArtistsDTO = new ArrayList<>();

            for (ArtistDTO artistDTO: i.song().artists()){
                Artist existingArtist = existingArtists.get(artistDTO.idSpotify());
                //si existe, lo guardo en la lista de artistas existentes de esta cancion
                if (existingArtist!=null) {
                    songArtists.add(existingArtist);
                }
                //sino, lo guardo en la lista de nuevos artistas
                else{
                    newSongArtistsDTO.add(artistDTO);
                }
            }

            //creo los nuevos artistas y luego los agrego a la lista de artistas de la cancion
            List<Artist> buildingArtists = artistService.buildArtists(newSongArtistsDTO, accessToken);
            songArtists.addAll(buildingArtists);

            //tambien los agrego a la lista de nuevos artistas y artistas existentes
            newArtists.addAll(buildingArtists);
            buildingArtists.stream().forEach(a-> existingArtists.put(a.getIdSpotify(), a));

            //----------------------------------------------------------------------------------------------------------
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

            //----------------------------------------------------------------------------------------------------------
            //REPRODUCTION
            Reproduction reproduction = reproductionService.buildReproduction(i, user, song);
            newReproductions.add(reproduction);

        }

        //Guardo por lotes las entidades nuevas
        artistService.saveAll(newArtists);
        albumService.saveAll(newAlbums);
        songService.saveAll(newSongs);

        newReproductions.stream().forEach(r->reproductionService.saveReproduction(r));//no necesariamente son todas nuevas, el metodo verifica si no existen con anterioridad
    }


}
