package com.emijordan.Spotinsights.repository;

import com.emijordan.Spotinsights.entities.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Optional<Artist> findByIdSpotify(String idSpotify);
    List<Artist> findByIdSpotifyIn(Set artistIds);
}
