package com.emijordan.Spotinsights.repository;

import com.emijordan.Spotinsights.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByIdSpotify(String idSpotify);

    List<Song> findByIdSpotifyIn(Set songIds);
}
