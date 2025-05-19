package com.example.moviecollection.service;

import com.example.moviecollection.model.AppUser;
import com.example.moviecollection.model.Movie;
import com.example.moviecollection.repository.AppUserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    private final AppUserRepository appUserRepository;

    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
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
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        Movie movie = new Movie();
        movie.setId(movieId); // zakładamy, że istnieje
        user.getWatchedMovies().add(movie);
        return appUserRepository.save(user);
    }

    public AppUser rateMovie(Long userId, Long movieId, int rating) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        Movie movie = new Movie();
        movie.setId(movieId);
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
}
