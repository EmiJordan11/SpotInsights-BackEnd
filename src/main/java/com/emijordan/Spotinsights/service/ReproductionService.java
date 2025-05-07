package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.dto.ItemDTO;
import com.emijordan.Spotinsights.entities.Reproduction;
import com.emijordan.Spotinsights.entities.Song;
import com.emijordan.Spotinsights.entities.User;
import com.emijordan.Spotinsights.repository.ReproductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class ReproductionService {

    @Autowired
    private ReproductionRepository reproductionRepository;

    public Reproduction buildReproduction(ItemDTO itemDTO, User user, Song song){
        LocalDateTime dateTime = formatDateTime(itemDTO.played_at());
        return new Reproduction(itemDTO, dateTime, user, song);
    }

    public Reproduction saveReproduction(Reproduction reproduction){
        Optional<Reproduction> existingReproduction = reproductionRepository.findByUserIdAndSongIdAndDateTime(reproduction.getUser().getId(), reproduction.getSong().getId(), reproduction.getDateTime());

        if (existingReproduction.isEmpty()){
            Reproduction persistedReproduction = reproductionRepository.save(reproduction);
            return persistedReproduction;
        }
        return existingReproduction.get();
    }

    public LocalDateTime formatDateTime(String dateTime){
        Instant instant = Instant.parse(dateTime);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }


    public void saveAll(List<Reproduction> newReproductions) {
        reproductionRepository.saveAll(newReproductions);
    }

//    public Reproduction getReproduction(ItemDTO itemDTO, User user, Song song){
//        LocalDateTime dateTime = formatDateTime(itemDTO.played_at());
//        Reproduction reproduction = new Reproduction(itemDTO, dateTime, user, song);
//        return saveReproduction(reproduction);
//    }
}
