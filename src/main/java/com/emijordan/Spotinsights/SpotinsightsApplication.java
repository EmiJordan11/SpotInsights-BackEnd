package com.emijordan.Spotinsights;

import com.emijordan.Spotinsights.client.Mappers;
import com.emijordan.Spotinsights.client.SpotifyApiClient;
import com.emijordan.Spotinsights.dto.SongDTO;
import com.emijordan.Spotinsights.dto.SpotifyApiResponse;
import com.emijordan.Spotinsights.dto.UserDTO;
import com.emijordan.Spotinsights.service.SpotifyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class SpotinsightsApplication{

//	@Autowired
//	private SpotifyDataService service;

	public static void main(String[] args) {
		SpringApplication.run(SpotinsightsApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		service.getData();
//	}

}
