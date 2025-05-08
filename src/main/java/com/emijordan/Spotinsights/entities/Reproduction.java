package com.emijordan.Spotinsights.entities;

import com.emijordan.Spotinsights.dto.ItemDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reproduction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dateTime;
    @ManyToOne
    private User user;
    @ManyToOne
    private Song song;

    public Reproduction(ItemDTO itemDTO, LocalDateTime dateTime, User user, Song song) {
        this.dateTime = dateTime;
        this.user = user;
        this.song = song;
    }

    @Override
    public String toString() {
        return "Reproduction{" + "\n" +
                "id: " + id + "\n" +
                "dateTime: " + dateTime + "\n" +
                "user: " + user.getName() + "\n" +
                "song: '" + song.getName() + "'\n" +
                "album: '" + song.getAlbum().getName() + "'\n" +
                "album artists: " + song.getAlbum().getArtists().stream()
                                    .map(a-> a.getName())
                                    .collect(Collectors.joining(", ")) + "\n" +
                "artists: " + song.getArtists().stream()
                                .map(a-> a.getName())
                                .collect(Collectors.joining(" ,")) + "\n" +
                '}';
    }
}
