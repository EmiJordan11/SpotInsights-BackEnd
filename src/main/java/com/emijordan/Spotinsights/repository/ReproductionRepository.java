package com.emijordan.Spotinsights.repository;

import com.emijordan.Spotinsights.entities.Reproduction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReproductionRepository extends JpaRepository<Reproduction, Long> {
    Optional<Reproduction> findByUserIdAndSongIdAndDateTime(Long idUser, Long idSong, LocalDateTime dateTime);
}
