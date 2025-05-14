package com.emijordan.Spotinsights.service.auth;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.dto.TokenResponse;
import com.emijordan.Spotinsights.service.SpotifyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Service
public class SpotifyAuthService {

    @Value("${encoded.credentials}")
    private String encodedCredentials;

    @Value("${redirect.uri}")
    private String redirectUri;

    @Autowired
    private SpotifyDataService spotifyDataService;

    public void spotifyAuth(String code) {
        String response = getSpotifyTokens(code);

        TokenResponse spotifyTokens = Mappers.convertData(response, TokenResponse.class);
        spotifyDataService.getData(spotifyTokens);
    }

    public String getSpotifyTokens(String code){
        HttpClient client = HttpClient.newHttpClient();

        //body
        String requestBody = "grant_type=authorization_code"
                + "&code=" + URLEncoder.encode(code, StandardCharsets.UTF_8)
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://accounts.spotify.com/api/token"))
                .headers("Content-type", "application/x-www-form-urlencoded")
                .header("Authorization", "Basic " + encodedCredentials)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = null;
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode()==200){
                return response.body();
            } else{
//                throw new RuntimeException("Error al obtener los token de Spotify: " + response.body());
                throw new RuntimeException(response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
