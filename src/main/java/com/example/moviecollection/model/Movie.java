package com.example.moviecollection.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.NotNull;


@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    @Size(min = 10, message = "Description must be at least 10 characters long")
    private String description;

    @Min(1900)
    @Max(2100)
    private int releaseYear;

    @NotNull
    @ManyToOne
    private Director director;

    @NotNull
    @ManyToOne
    private Genre genre;

    public Movie() {}

    public Movie(Long id, String title, String description, int releaseYear, Director director, Genre genre) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.director = director;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id != null && id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
