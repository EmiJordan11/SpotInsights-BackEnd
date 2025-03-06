package com.emijordan.Spotinsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SongDTO(
        String name,
        int popularity,
        @JsonProperty("duration_ms")
        int durationMs,
        AlbumDTO album,
        List<ArtistDTO> artists,
        @JsonProperty("id")
        String idSpotify
) {
        @Override
        public String toString() {
                return "SongDTO{" + "\n" +
                        "name: " + name+ "\n" +
                        "album: " + album.name() + "\n" +
                        "popularity: " + popularity + "\n" +
                        "durationMs: " + durationMs + "\n" +
                        "artists: " + artists.stream()
                                .map(ArtistDTO::toString)
                                .collect(Collectors.joining(", ")) + "\n" +
                        "idSpotify: " + idSpotify + "\n" +
                        '}';
        }
}
