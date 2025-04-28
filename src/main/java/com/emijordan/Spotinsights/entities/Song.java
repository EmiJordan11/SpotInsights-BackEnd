package com.emijordan.Spotinsights.entities;

import com.emijordan.Spotinsights.dto.SongDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int popularity;
    private int durationMs;
    @ManyToOne
    private Album album;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "song_artist",
            joinColumns = @JoinColumn(name = "id_song"),
            inverseJoinColumns = @JoinColumn(name = "id_artist")
    )
    private List<Artist> artists;
    private String idSpotify;


    public Song(SongDTO songDTO, Album album, List<Artist> artists) {
        this.name = songDTO.name();
        this.popularity = songDTO.popularity();
        this.durationMs = songDTO.durationMs();
        this.album = album;
        this.artists = artists;
        this.idSpotify = songDTO.idSpotify();
    }

    @Override
    public String toString() {
        return "Song{" + '\n' +
                "id: " + id + '\n' +
                "name: " + name + '\n' +
                "popularity: " + popularity + '\n' +
                "durationMs: " + durationMs + '\n' +
                "album name: " + album.getName() + '\n' +
                "artists: " + artists.stream()
                            .map(a-> a.getName())
                            .collect(Collectors.joining(", ")) + '\n' +
                "idSpotify: " + idSpotify + '\n' +
                '}';
    }
}
