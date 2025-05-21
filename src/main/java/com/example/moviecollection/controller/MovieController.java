package com.example.moviecollection.controller;

import com.example.moviecollection.model.Movie;
import com.example.moviecollection.service.MovieService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/{id}")
    public Optional<Movie> getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping
    public ResponseEntity<Movie> addMovie(@Valid @RequestBody Movie movie) {
        return ResponseEntity.ok(movieService.saveMovie(movie));
    }


    @DeleteMapping("/{id}")
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @GetMapping("/genre/{genreName}")
    public List<Movie> getMoviesByGenre(@PathVariable String genreName) {
        return movieService.getMoviesByGenre(genreName);
    }

    @GetMapping("/director/{directorName}")
    public List<Movie> getMoviesByDirector(@PathVariable String directorName) {
        return movieService.getMoviesByDirector(directorName);
    }

    @GetMapping("/sorted/year/asc")
    public List<Movie> getMoviesSortedByYearAsc() {
        return movieService.getMoviesSortedByYearAsc();
    }

    @GetMapping("/sorted/year/desc")
    public List<Movie> getMoviesSortedByYearDesc() {
        return movieService.getMoviesSortedByYearDesc();
    }

    @GetMapping("/search/title")
    public List<Movie> searchByTitle(@RequestParam String title) {
        return movieService.searchByTitle(title);
    }

    @GetMapping("/search/description")
    public List<Movie> searchByDescription(@RequestParam String desc) {
        return movieService.searchByDescription(desc);
    }

    @PutMapping("/{id}")
    public Movie updateMovie(@PathVariable Long id, @RequestBody Movie updatedMovie) {
        return movieService.updateMovie(id, updatedMovie);
    }

}
