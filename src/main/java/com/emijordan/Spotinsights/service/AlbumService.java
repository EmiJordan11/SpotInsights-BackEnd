package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.dto.AlbumDTO;
import com.emijordan.Spotinsights.entities.Album;
import com.emijordan.Spotinsights.entities.Artist;
import com.emijordan.Spotinsights.repository.AlbumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AlbumService {

    @Autowired
    private AlbumRepository albumRepository;

    public Album getAlbum(AlbumDTO albumDTO, Artist artist){
        Album album = new Album(albumDTO, artist);
        Album existAlbum = saveAlbum(album);
        return existAlbum;
    }

    public Album saveAlbum(Album album){
        Optional<Album> existingAlbum = albumRepository.findByIdSpotify(album.getIdSpotify());
        if (existingAlbum.isEmpty()){
            Album persistedAlbum = albumRepository.save(album);
            return persistedAlbum;
        }
        return existingAlbum.get();
    }

}
