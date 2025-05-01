package com.emijordan.Spotinsights.controller;


import com.emijordan.Spotinsights.service.auth.SpotifyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync-data")
public class DataSyncController {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @PostMapping
    public ResponseEntity syncData(@RequestParam(name = "code") String code){
        spotifyAuthService.spotifyAuth(code);
        return ResponseEntity.noContent().build();
    }
}
