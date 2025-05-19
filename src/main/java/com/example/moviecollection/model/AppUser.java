package com.example.moviecollection.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @ManyToMany
    private List<Movie> watchedMovies;

    @ElementCollection
    @CollectionTable(name = "user_movie_ratings")
    @MapKeyJoinColumn(name = "movie_id")
    @Column(name = "rating")
    private Map<Movie, Integer> ratings;
}
