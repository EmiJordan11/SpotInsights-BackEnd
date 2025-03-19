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

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    public Song getSong(ItemDTO itemDTO, Album album, List<Artist> artists){
        Song song = new Song(itemDTO.song(), album, artists);
        Song existSong = saveSong(song);
        return existSong; //cambiar nombre
    }

    public Song saveSong(Song song){
        Optional<Song> existingSong = songRepository.findByIdSpotify(song.getIdSpotify());
        if (existingSong.isEmpty()){
            Song persistedSong = songRepository.save(song);
            return persistedSong;
        }
        return existingSong.get();
    }

}
