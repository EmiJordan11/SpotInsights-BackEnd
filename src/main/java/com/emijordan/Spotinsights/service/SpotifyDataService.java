package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.client.SpotifyApiClient;
import com.emijordan.Spotinsights.dto.*;
import com.emijordan.Spotinsights.entities.*;
import com.emijordan.Spotinsights.repository.SongRepository;
import com.emijordan.Spotinsights.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public void getData(){
        //User
        User user = userService.getUser();

        SpotifyApiResponse response = getApiResponse();
        for (ItemDTO i: response.items()){
            //Album artist
            Artist albumArtist = artistService.getAlbumArtist(i);

            //Album
            Album album = albumService.getAlbum(i.song().album(), albumArtist);

            //Song artists
            List<Artist> artists = artistService.getArtist(i);

            //Song
            Song song = songService.getSong(i, album, artists);

            //Reproduction
            Reproduction reproduction = reproductionService.getReproduction(i, user, song);
            System.out.println(reproduction);
        }
    }

    public SpotifyApiResponse getApiResponse(){
        String json = spotifyApiClient.getRecentlyPlayed();
        SpotifyApiResponse response = Mappers.convertData(json, SpotifyApiResponse.class);
        return response;
    }

}
