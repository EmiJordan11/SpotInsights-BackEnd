package com.emijordan.Spotinsights.entities;

import com.emijordan.Spotinsights.dto.ArtistDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "artist_genre",
            joinColumns = @JoinColumn(name = "id_artist"),
            inverseJoinColumns = @JoinColumn(name = "id_genre")
    )
    private List<Genre> genres;
    private String idSpotify;

    public Artist(ArtistDTO artistDTO) {
        this.name = artistDTO.name();
        this.idSpotify = artistDTO.idSpotify();
        this.genres = artistDTO.genres().stream()
                .map(Genre::new)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Artist{" + "\n" +
                "id: " + id + "\n" +
                "name: " + name + "\n" +
                "genres: " + genres.stream()
                    .map(Genre::getName)
                    .collect(Collectors.joining(", "))+ "\n" +
                "idSpotify: " + idSpotify + "\n" +
                '}';
    }
}
