package com.example.moviecollection.controller;

import com.example.moviecollection.model.Genre;
import com.example.moviecollection.service.GenreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @PostMapping
    public Genre addGenre(@RequestBody Genre genre) {
        return genreService.saveGenre(genre);
    }
}
