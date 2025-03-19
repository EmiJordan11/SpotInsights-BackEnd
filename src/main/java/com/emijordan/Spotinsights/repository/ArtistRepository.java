package com.emijordan.Spotinsights.repository;

import com.emijordan.Spotinsights.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByIdSpotify(String idSpotify);
}
