package com.example.moviecollection.repository;

import com.example.moviecollection.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenre_Name(String genreName);
    List<Movie> findByDirector_FullName(String directorName);
    List<Movie> findAllByOrderByReleaseYearAsc();
    List<Movie> findAllByOrderByReleaseYearDesc();
    List<Movie> findByTitleContainingIgnoreCase(String title);
    List<Movie> findByDescriptionContainingIgnoreCase(String desc);

}
