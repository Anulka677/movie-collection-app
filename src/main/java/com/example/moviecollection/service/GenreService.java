package com.example.moviecollection.service;

import com.example.moviecollection.model.Genre;
import com.example.moviecollection.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Genre updateGenre(Long id, Genre updatedGenre) {
        Genre existing = genreRepository.findById(id).orElseThrow();
        existing.setName(updatedGenre.getName());
        return genreRepository.save(existing);
    }

    public void deleteGenre(Long id) {
        genreRepository.deleteById(id);
    }

}
