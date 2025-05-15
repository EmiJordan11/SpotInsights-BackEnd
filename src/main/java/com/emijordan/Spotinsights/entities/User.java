package com.emijordan.Spotinsights.entities;

import com.emijordan.Spotinsights.dto.UserDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "\"User\"")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String idSpotify;
    private String refreshToken;
    private LocalDateTime deleteAt;

    public User(UserDTO userDTO, String userRefreshToken) {
        this.name = userDTO.name();
        this.idSpotify = userDTO.idSpotify();
        this.refreshToken = userRefreshToken;
    }

    @Override
    public String toString() {
        return "User{" + '\n' +
                "id: " + id + '\n' +
                "name: " + name + '\n' +
                "idSpotify: " + idSpotify + '\n' +
                '}';
    }
}
