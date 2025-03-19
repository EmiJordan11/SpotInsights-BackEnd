package com.emijordan.Spotinsights.repository;

import com.emijordan.Spotinsights.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByIdSpotify(String idSpotify);
}
