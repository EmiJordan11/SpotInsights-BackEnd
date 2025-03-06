package com.emijordan.Spotinsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record UserDTO(
        @JsonProperty("display_name")
        String name,
        @JsonProperty("id")
        String idSpotify
) {
    @Override
    public String toString() {
        return "UserDTO{" + '\n' +
                "name: " + name + '\n' +
                "idSpotify: " + idSpotify + '\n' +
                '}';
    }
}
