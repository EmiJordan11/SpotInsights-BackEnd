package com.emijordan.Spotinsights.repository;

import com.emijordan.Spotinsights.entities.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SongRepository extends JpaRepository<Song, Long> {
    Optional<Song> findByIdSpotify(String idSpotify);
}
