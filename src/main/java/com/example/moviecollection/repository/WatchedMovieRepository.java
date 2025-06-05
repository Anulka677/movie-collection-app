package com.example.moviecollection.repository;

import com.example.moviecollection.model.WatchedMovie;
import com.example.moviecollection.model.AppUser;
import com.example.moviecollection.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface WatchedMovieRepository extends JpaRepository<WatchedMovie, Long> {
    List<WatchedMovie> findByUser(AppUser user);
    List<WatchedMovie> findByUserAndWatchedDateBetween(AppUser user, LocalDate start, LocalDate end);
    boolean existsByUserAndMovie(AppUser user, Movie movie);
}
