package com.emijordan.Spotinsights.controller;


import com.emijordan.Spotinsights.service.ScheduleTask;
import com.emijordan.Spotinsights.service.auth.SpotifyAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sync-data")
public class DataSyncController {

    @Autowired
    private SpotifyAuthService spotifyAuthService;

    @Autowired
    private ScheduleTask scheduleTask;

    private static final Logger logger = LoggerFactory.getLogger(DataSyncController.class);

    @PostMapping
    public ResponseEntity syncData(@RequestParam(name = "code") String code){
        logger.info("🟢 INICIO PROCESO POST LOGIN: nuevo usuario logueado desde el Front\n");
        spotifyAuthService.spotifyAuth(code);
        logger.info("\n🔴 FIN DEL PROCESO POST LOGIN: Datos obtenidos correctamente\n");
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/scheduler")
    public ResponseEntity updateData(){
        scheduleTask.updateReproductions();
        return ResponseEntity.noContent().build();
    }

}
