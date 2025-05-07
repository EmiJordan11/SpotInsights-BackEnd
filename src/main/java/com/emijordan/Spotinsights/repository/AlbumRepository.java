package com.emijordan.Spotinsights.repository;

import com.emijordan.Spotinsights.entities.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByIdSpotify(String idSpotify);

    List<Album> findByIdSpotifyIn(Set albumIds);
}
