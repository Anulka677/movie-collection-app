package com.example.moviecollection.controller;

import com.example.moviecollection.model.AppUser;
import com.example.moviecollection.service.AppUserService;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

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
    public Optional<AppUser> getUserById(@PathVariable Long id) {
        return appUserService.getUserById(id);
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
    public AppUser updateUser(@PathVariable Long id, @RequestBody AppUser updatedUser) {
        return appUserService.updateUser(id, updatedUser);
    }


}
