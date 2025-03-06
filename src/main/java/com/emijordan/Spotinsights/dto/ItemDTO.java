package com.emijordan.Spotinsights.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ItemDTO(
        @JsonProperty("track")
        SongDTO song,
//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        String played_at
) {
        @Override
        public String toString() {
                return "ItemDTO{" + "\n" +
                        "song: " + song + "\n" +
                        "played_at: " + played_at + "\n" +
                        '}';
        }
}
