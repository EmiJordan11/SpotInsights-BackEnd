package com.emijordan.Spotinsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SongDTO(
        String name,
        @JsonProperty("id")
        String id_spotify
) {
        @Override
        public String toString() {
                return "SongDTO{" + "\n" +
                        "name: " + name+ "\n" +
                        "id_spotify: " + id_spotify + "\n" +
                        '}';
        }
}
