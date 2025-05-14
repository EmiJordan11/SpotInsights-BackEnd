package com.emijordan.Spotinsights.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AccessTokenDTO(
        @JsonProperty("access_token")
        String accessToken
) {
}
