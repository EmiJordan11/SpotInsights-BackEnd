package com.emijordan.Spotinsights.client;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

//@AllArgsConstructor
//@NoArgsConstructor
@Component
public class SpotifyApiClient {
    @Value("${access.token}")
    private String auth_token;

    public String get_artist(){

        System.out.println(auth_token);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.spotify.com/v1/me/player/recently-played?limit=2"))
                .headers("Authorization", "Bearer " + auth_token)
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
}
