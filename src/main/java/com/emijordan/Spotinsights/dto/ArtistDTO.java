package com.emijordan.Spotinsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ArtistDTO(
        String name,
        List<String> genres,
        @JsonProperty("id")
        String idSpotify
) {
}
