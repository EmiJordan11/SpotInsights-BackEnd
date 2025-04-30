package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.client.SpotifyApiClient;
import com.emijordan.Spotinsights.dto.UserDTO;
import com.emijordan.Spotinsights.entities.User;
import com.emijordan.Spotinsights.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private SpotifyApiClient spotifyApiClient;

    @Autowired
    private UserRepository userRepository;

    public User getUser(String userRefreshToken){
        UserDTO userDTO = getUserFromApi();
        User user = saveUser(userDTO, userRefreshToken);
        return user;
    }

    public User saveUser(UserDTO userDTO, String userRefreshToken){
        User user = new User(userDTO, userRefreshToken);
        Optional<User> existingUser = userRepository.findByIdSpotify(user.getIdSpotify());
        if (existingUser.isEmpty()){
            User persistedUser = userRepository.save(user);
            return persistedUser;
        }
        return existingUser.get();
    }

    public UserDTO getUserFromApi(){
        String userJson = spotifyApiClient.getUser();
        UserDTO userDTO = Mappers.convertData(userJson, UserDTO.class);
        return userDTO;
    }
}
