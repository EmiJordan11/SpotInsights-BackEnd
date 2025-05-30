package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.controller.DataSyncController;
import com.emijordan.Spotinsights.dto.AccessTokenDTO;
import com.emijordan.Spotinsights.entities.User;
import com.emijordan.Spotinsights.repository.UserRepository;
import com.emijordan.Spotinsights.service.auth.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleTask {
    @Value("${encoded.credentials}")
    private String encodedCredentials;

    @Value("${client.id}")
    private String clientId;

    @Value("${client.secret}")
    private String clientSecret;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SpotifyDataService spotifyDataService;

    private static final Logger logger = LoggerFactory.getLogger(DataSyncController.class);

    public void updateReproductions(){
        logger.info("🟢 INICIO DE LA TAREA AUTOMATIZADA: Obteniendo nuevos datos\n");

        List<User> users = userRepository.findByDeleteAtIsNull();
        if (users != null) {
            for (User user : users){
                String refreshTokenDecrypt = tokenService.decryptToken(user.getRefreshToken());

                try{
                    String newAccesToken = getNewAccesToken(refreshTokenDecrypt).accessToken();
                    spotifyDataService.scheduledSyncForExistingUsers(newAccesToken, user);
                } catch (RuntimeException e) {
                    logger.debug("Usuario dado de baja: " + user.getName());

                    user.setDeleteAt(LocalDateTime.now());
                    userRepository.save(user);
                }
            }
        }
        logger.info("\n🔴 FIN DE LA TAREA AUTOMATIZADA: Datos nuevos obtenidos correctament\n");
    }

    public AccessTokenDTO getNewAccesToken(String refreshToken){
        HttpClient client = HttpClient.newHttpClient();

        String requestBody =
                "grant_type=refresh_token"
                + "&refresh_token=" + URLEncoder.encode(refreshToken, StandardCharsets.UTF_8)
                + "&client_id=" + URLEncoder.encode(clientId, StandardCharsets.UTF_8)
                + "&client_secret=" + URLEncoder.encode(clientSecret, StandardCharsets.UTF_8);

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
                //mappeo el access token
                return Mappers.convertData(response.body(), AccessTokenDTO.class);
            } else{
//                throw new RuntimeException("Error al obtener los token de Spotify: " + response.body());
                throw new RuntimeException(response.body());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
