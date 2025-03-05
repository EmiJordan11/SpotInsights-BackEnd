package com.emijordan.Spotinsights.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ManyToAny;

import java.util.List;

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
    private String idSpotify;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "artist_genre",
            joinColumns = @JoinColumn(name = "id_artist"),
            inverseJoinColumns = @JoinColumn(name = "id_genre")
    )
    private List<Genre> genres;
}
