package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.dto.AlbumDTO;
import com.emijordan.Spotinsights.entities.Album;
import com.emijordan.Spotinsights.entities.Artist;
import com.emijordan.Spotinsights.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public List<Album> getExistingAlbums(Set albumIds){
        return albumRepository.findByIdSpotifyIn(albumIds);
    }

    public Album buildAlbum(AlbumDTO albumDTO, List<Artist> artists){
        return new Album(albumDTO, artists);
    }

    public void saveAll(List<Album> newAlbums) {
        albumRepository.saveAll(newAlbums);
    }

}
