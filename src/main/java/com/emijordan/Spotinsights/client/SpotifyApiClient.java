package com.emijordan.Spotinsights.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//@AllArgsConstructor
//@NoArgsConstructor
@Setter
@Component
public class SpotifyApiClient {

    private String authToken;

    public String getApiResponse(String url){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Authorization", "Bearer " + authToken)
                .GET()
                .build();

        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()==200){
                return response.body();
            }

            System.out.println(response.statusCode());

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    public String getUser(){
        return getApiResponse("https://api.spotify.com/v1/me");
    }

    public String getRecentlyPlayed(){
        return getApiResponse("https://api.spotify.com/v1/me/player/recently-played?limit=50");
    }

    public String getArtist(String idArtist){
        return getApiResponse("https://api.spotify.com/v1/artists/" + idArtist);
    }
}
