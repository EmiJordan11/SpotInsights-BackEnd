package com.emijordan.Spotinsights.entities;

import com.emijordan.Spotinsights.dto.AlbumDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "album_artist",
            joinColumns = @JoinColumn(name = "id_album"),
            inverseJoinColumns = @JoinColumn(name = "id_artist")
    )
    private List<Artist> artists;

    public Album(AlbumDTO albumDTO, List<Artist> artist) {
        this.name = albumDTO.name();
        this.type = albumDTO.type();
        this.idSpotify = albumDTO.idSpotify();
        this.artists = artist;
    }

    @Override
    public String toString() {
        return "Album{" + "\n" +
                "id: " + id + "\n" +
                "name: " + name + "\n" +
                "type: " + type + "\n" +
                "idSpotify: " + idSpotify + "\n" +
                "artists: " + artists.stream()
                            .map(a-> a.getName())
                            .collect(Collectors.joining(", ")) + '\n' +
                '}';
    }
}
