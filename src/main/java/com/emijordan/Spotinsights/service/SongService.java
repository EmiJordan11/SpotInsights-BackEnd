package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.dto.ItemDTO;
import com.emijordan.Spotinsights.entities.Album;
import com.emijordan.Spotinsights.entities.Artist;
import com.emijordan.Spotinsights.entities.Song;
import com.emijordan.Spotinsights.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;


    public List<Song> getExistingSongs(Set songIds){
        return songRepository.findByIdSpotifyIn(songIds);
    }

    public Song buildSong(ItemDTO itemDTO, Album album, List<Artist> artists){
        return new Song(itemDTO.song(), album, artists);
    }

    public void saveAll(List<Song> newSongs) {
        songRepository.saveAll(newSongs);
    }



//    public Song saveSong(Song song){
//        Song persistedSong = songRepository.save(song);
//        return persistedSong;
//    }

//    public Song getSong(ItemDTO itemDTO, Album album, List<Artist> artists){
//        Song song = new Song(itemDTO.song(), album, artists);
//        Song existSong = saveSong(song);
//        return existSong; //cambiar nombre
//    }


}
