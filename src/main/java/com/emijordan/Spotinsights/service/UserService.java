package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.client.SpotifyApiClient;
import com.emijordan.Spotinsights.dto.UserDTO;
import com.emijordan.Spotinsights.entities.User;
import com.emijordan.Spotinsights.repository.UserRepository;
import com.emijordan.Spotinsights.service.auth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    public User getUser(String userRefreshToken){
        UserDTO userDTO = getUserFromApi();
        //BORRAR
        System.out.println("Refresh Token: " + userRefreshToken);
        //
        String encryptRefreshToken = tokenService.encryptToken(userRefreshToken);

        User user = saveUser(userDTO, encryptRefreshToken);
        return user;
    }

    public User saveUser(UserDTO userDTO, String encryptRefreshToken){
        User user = new User(userDTO, encryptRefreshToken);
        Optional<User> existingUser = userRepository.findByIdSpotify(user.getIdSpotify());
        if (existingUser.isEmpty()){
            User persistedUser = userRepository.save(user);
            return persistedUser;
        }
        existingUser.get().setRefreshToken(encryptRefreshToken); //actualizo el refresh token
        return existingUser.get();
    }

    public UserDTO getUserFromApi(){
        String userJson = spotifyApiClient.getUser();
        UserDTO userDTO = Mappers.convertData(userJson, UserDTO.class);
        return userDTO;
    }
}
