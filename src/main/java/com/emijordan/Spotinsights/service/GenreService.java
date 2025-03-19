package com.emijordan.Spotinsights.service;

import com.emijordan.Spotinsights.entities.Genre;
import com.emijordan.Spotinsights.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {
    @Autowired
    private GenreRepository genreRepository;

    public List<Genre> getGenres(List<String> genresString){
        List<Genre> genres = genresString.stream()
                .map(Genre::new)
                .toList();

        List<Genre> existsGenres = genres.stream()
                .map(g-> saveGenre(g))
                .toList();

        return existsGenres;
    }

    public Genre saveGenre(Genre genre){
        Optional<Genre> existingGenre = genreRepository.findByName(genre.getName());
        if (existingGenre.isEmpty()){
            Genre persistedGenre = genreRepository.save(genre);
            return persistedGenre;
        }
        return existingGenre.get();
    }

}
