package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.client.SpotifyApiClient;
import com.emijordan.Spotinsights.dto.ArtistDTO;
import com.emijordan.Spotinsights.dto.ItemDTO;
import com.emijordan.Spotinsights.entities.Artist;
import com.emijordan.Spotinsights.entities.Genre;
import com.emijordan.Spotinsights.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArtistService {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private GenreService genreService;

    //obtengo un nuevo dto ya que el anterior no tiene los generos
    public ArtistDTO getArtistFromApi(String idArtist){
        String json = spotifyApiClient.getArtist(idArtist);
        ArtistDTO artistDTO = Mappers.convertData(json, ArtistDTO.class);
        return artistDTO;
    }

    public List<Artist> getExistingArtists(Set artistIds) {
        return artistRepository.findByIdSpotifyIn(artistIds);
    }

    public List<Artist> buildArtists(List<ArtistDTO> newArtistDTOList){
        //los artistDto que tengo no tienen los generos, debo hacer una llamada especifica a la API para obtenerlos
        List<ArtistDTO> artistsDTOS = newArtistDTOList.stream()
                .map(artistDTO -> getArtistFromApi(artistDTO.idSpotify()))
                .toList();

        //mappeo los dto a entidades
        List<Artist> artists = artistsDTOS.stream()
                .map(Artist::new)
                .toList();

        //verifico si los generos ya estan en la bd y los vinculo
        for (Artist artist: artists){
            for (int i = 0; i < artist.getGenres().size(); i++) {
                Genre persistedGenre = genreService.saveGenre(artist.getGenres().get(i));
                artist.getGenres().set(i, persistedGenre);
            }
        }

        return artists;
    }

    public void saveAll(List<Artist> artists) {
        artistRepository.saveAll(artists);
    }
}