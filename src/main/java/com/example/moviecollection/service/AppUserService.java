package com.example.moviecollection.service;

import com.example.moviecollection.model.AppUser;
import com.example.moviecollection.model.Movie;
import com.example.moviecollection.repository.AppUserRepository;
import com.example.moviecollection.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.NoSuchElementException;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;
    private final MovieRepository movieRepository;

    public AppUserService(AppUserRepository appUserRepository, MovieRepository movieRepository) {
        this.appUserRepository = appUserRepository;
        this.movieRepository = movieRepository;
    }

    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return appUserRepository.findById(id);
    }

    public AppUser saveUser(AppUser user) {
        return appUserRepository.save(user);
    }

    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }


    public AppUser markMovieAsWatched(Long userId, Long movieId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        user.getWatchedMovies().add(movie);
        return appUserRepository.save(user);
    }

    public AppUser rateMovie(Long userId, Long movieId, int rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        user.getRatings().put(movie, rating);
        return appUserRepository.save(user);
    }

    public String getFavoriteGenre(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        return user.getWatchedMovies().stream()
                .map(movie -> movie.getGenre().getName())
                .collect(Collectors.groupingBy(g -> g, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Brak");
    }

    public String getMostWatchedDirector(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        return user.getWatchedMovies().stream()
                .map(movie -> movie.getDirector().getFullName())
                .collect(Collectors.groupingBy(d -> d, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Brak");
    }

    public AppUser updateUser(Long id, AppUser updatedUser) {
        AppUser existing = appUserRepository.findById(id).orElseThrow();
        existing.setUsername(updatedUser.getUsername());
        return appUserRepository.save(existing);
    }

}
