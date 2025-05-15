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

@Setter
@Component
public class SpotifyApiClient {

    public String getApiResponse(String url, String accessToken){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .headers("Authorization", "Bearer " + accessToken)
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

    public String getUser(String accessToken){
        return getApiResponse("https://api.spotify.com/v1/me", accessToken);
    }

    public String getRecentlyPlayed(String accessToken){
        return getApiResponse("https://api.spotify.com/v1/me/player/recently-played?limit=50", accessToken);
    }

    public String getArtist(String idArtist, String accessToken){
        return getApiResponse("https://api.spotify.com/v1/artists/" + idArtist, accessToken);
    }

}
