package com.emijordan.Spotinsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AlbumDTO(
        String name,
        List<ArtistDTO> artists,
        @JsonProperty("album_type")
        String type,
        @JsonProperty("id")
        String idSpotify
) {
}
