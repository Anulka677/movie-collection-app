package com.example.moviecollection.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

@Entity
public class WatchedMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @NotNull
    @PastOrPresent
    private LocalDate watchedDate;

    @Min(1)
    @Max(10)
    private Integer rating;

    public WatchedMovie() {}

    public WatchedMovie(AppUser user, Movie movie, LocalDate watchedDate) {
        this.user = user;
        this.movie = movie;
        this.watchedDate = watchedDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public LocalDate getWatchedDate() {
        return watchedDate;
    }

    public void setWatchedDate(LocalDate watchedDate) {
        this.watchedDate = watchedDate;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
