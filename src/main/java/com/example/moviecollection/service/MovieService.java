package com.example.moviecollection.service;

import com.example.moviecollection.model.Movie;
import com.example.moviecollection.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(Long id) {
        return movieRepository.findById(id);
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<Movie> getMoviesByGenre(String genreName) {
        return movieRepository.findByGenre_Name(genreName);
    }

    public List<Movie> getMoviesByDirector(String directorName) {
        return movieRepository.findByDirector_FullName(directorName);
    }

    public List<Movie> getMoviesSortedByYearAsc() {
        return movieRepository.findAllByOrderByReleaseYearAsc();
    }

    public List<Movie> getMoviesSortedByYearDesc() {
        return movieRepository.findAllByOrderByReleaseYearDesc();
    }

    public List<Movie> searchByTitle(String title) {
        return movieRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Movie> searchByDescription(String desc) {
        return movieRepository.findByDescriptionContainingIgnoreCase(desc);
    }
}
