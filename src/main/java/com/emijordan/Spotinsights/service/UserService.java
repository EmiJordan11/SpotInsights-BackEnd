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

        //setteo el nuevo refresh token y elimino la fecha de baja si es que era distinta de null (sino no afecta, sigue siendo null)
        existingUser.get().setDeleteAt(null);
        existingUser.get().setRefreshToken(encryptRefreshToken); //actualizo el refresh token

        User updateUser = userRepository.save(existingUser.get());
        return updateUser;
    }

    public UserDTO getUserFromApi(){
        String userJson = spotifyApiClient.getUser();
        UserDTO userDTO = Mappers.convertData(userJson, UserDTO.class);
        return userDTO;
    }
}
