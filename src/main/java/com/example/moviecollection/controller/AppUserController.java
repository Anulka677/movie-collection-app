package com.example.moviecollection.controller;

import com.example.moviecollection.model.AppUser;
import com.example.moviecollection.model.Movie;
import com.example.moviecollection.service.AppUserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/users")
public class AppUserController {

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping
    public List<AppUser> getAllUsers() {
        return appUserService.getAllUsers();
    }

    @GetMapping("/{id}")
    public AppUser getUserById(@PathVariable Long id) {
        return appUserService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
    }

    @PostMapping
    public ResponseEntity<AppUser> addUser(@Valid @RequestBody AppUser user) {
        return ResponseEntity.ok(appUserService.saveUser(user));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
    }

    @PutMapping("/{userId}/watch/{movieId}")
    public AppUser markMovieAsWatched(@PathVariable Long userId, @PathVariable Long movieId) {
        return appUserService.markMovieAsWatched(userId, movieId);
    }

    @PutMapping("/{userId}/watch/{movieId}/date")
    public AppUser markMovieAsWatchedWithDate(
            @PathVariable Long userId,
            @PathVariable Long movieId,
            @RequestParam String date
    ) {
        return appUserService.markMovieAsWatchedWithDate(userId, movieId, LocalDate.parse(date));
    }

    @PutMapping("/{userId}/rate/{movieId}")
    public AppUser rateMovie(@PathVariable Long userId, @PathVariable Long movieId, @RequestParam int rating) {
        return appUserService.rateMovie(userId, movieId, rating);
    }

    @GetMapping("/{userId}/stats/favorite-genre")
    public String getFavoriteGenre(@PathVariable Long userId) {
        return appUserService.getFavoriteGenre(userId);
    }

    @GetMapping("/{userId}/stats/most-watched-director")
    public String getMostWatchedDirector(@PathVariable Long userId) {
        return appUserService.getMostWatchedDirector(userId);
    }

    @PutMapping("/{id}")
    public AppUser updateUser(@PathVariable Long id, @Valid @RequestBody AppUser updatedUser) {
        return appUserService.updateUser(id, updatedUser);
    }

    @GetMapping("/{userId}/stats/average-rating")
    public double getAverageRating(@PathVariable Long userId) {
        return appUserService.getAverageUserRating(userId);
    }

    @GetMapping("/{userId}/stats/watched/weekly")
    public long getWeeklyWatchedMovies(@PathVariable Long userId) {
        return appUserService.getWatchedMoviesCountWeekly(userId);
    }

    @GetMapping("/{userId}/stats/watched/monthly")
    public long getMonthlyWatchedMovies(@PathVariable Long userId) {
        return appUserService.getWatchedMoviesCountMonthly(userId);
    }

    @GetMapping("/{userId}/stats/ratings/weekly")
    public double getAverageWeeklyRating(@PathVariable Long userId) {
        return appUserService.getAverageRatingWeekly(userId);
    }

    @GetMapping("/{userId}/stats/ratings/monthly")
    public double getAverageMonthlyRating(@PathVariable Long userId) {
        return appUserService.getAverageRatingMonthly(userId);
    }

    @GetMapping("/{userId}/stats/activity/weekly")
    public Map<LocalDate, Long> getWeeklyActivity(@PathVariable Long userId) {
        return appUserService.getWatchedActivityInLastDays(userId, 7);
    }

    @GetMapping("/{userId}/stats/activity/monthly")
    public Map<LocalDate, Long> getMonthlyActivity(@PathVariable Long userId) {
        return appUserService.getWatchedActivityInLastDays(userId, 30);
    }

    @GetMapping("/{userId}/stats/rating-histogram")
    public Map<Integer, Long> getRatingHistogram(@PathVariable Long userId) {
        return appUserService.getRatingHistogram(userId);
    }

    @GetMapping("/{userId}/stats/top-rated")
    public List<Movie> getTopRatedMovies(@PathVariable Long userId) {
        return appUserService.getTopRatedMovies(userId);
    }

    @GetMapping("/{userId}/stats/favorite-day")
    public String getFavoriteWatchDay(@PathVariable Long userId) {
        return appUserService.getFavoriteWatchDay(userId);
    }

    @GetMapping("/{userId}/stats/favorite-genre-watched")
    public String getMostWatchedGenre(@PathVariable Long userId) {
        return appUserService.getMostWatchedGenre(userId);
    }
}
