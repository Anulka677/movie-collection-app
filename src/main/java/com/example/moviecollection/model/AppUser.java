package com.example.moviecollection.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;


@Entity
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String username;

    @ManyToMany
    private List<Movie> watchedMovies;

    @ElementCollection
    @CollectionTable(name = "user_movie_ratings")
    @MapKeyJoinColumn(name = "movie_id")
    @Column(name = "rating")
    private Map<Movie, Integer> ratings;

    public AppUser() {
    }

    public AppUser(Long id, String username, List<Movie> watchedMovies, Map<Movie, Integer> ratings) {
        this.id = id;
        this.username = username;
        this.watchedMovies = watchedMovies;
        this.ratings = ratings;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Movie> getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(List<Movie> watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public Map<Movie, Integer> getRatings() {
        return ratings;
    }

    public void setRatings(Map<Movie, Integer> ratings) {
        this.ratings = ratings;
    }
}
