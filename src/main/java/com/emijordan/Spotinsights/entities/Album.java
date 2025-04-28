package com.emijordan.Spotinsights.entities;

import com.emijordan.Spotinsights.dto.AlbumDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    private String idSpotify;
    @ManyToOne
    private Artist artist;

    public Album(AlbumDTO albumDTO, Artist artist) {
        this.name = albumDTO.name();
        this.type = albumDTO.type();
        this.idSpotify = albumDTO.idSpotify();
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Album{" + "\n" +
                "id: " + id + "\n" +
                "name: " + name + "\n" +
                "type: " + type + "\n" +
                "idSpotify: " + idSpotify + "\n" +
                "artist name: " + artist.getName() + "\n" +
                '}';
    }
}
