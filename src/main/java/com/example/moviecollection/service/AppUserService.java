package com.example.moviecollection.service;

import com.example.moviecollection.model.AppUser;
import com.example.moviecollection.model.Movie;
import com.example.moviecollection.model.WatchedMovie;
import com.example.moviecollection.repository.AppUserRepository;
import com.example.moviecollection.repository.MovieRepository;
import com.example.moviecollection.repository.WatchedMovieRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final MovieRepository movieRepository;
    private final WatchedMovieRepository watchedMovieRepository;

    public AppUserService(AppUserRepository appUserRepository, MovieRepository movieRepository, WatchedMovieRepository watchedMovieRepository) {
        this.appUserRepository = appUserRepository;
        this.movieRepository = movieRepository;
        this.watchedMovieRepository = watchedMovieRepository;
    }

    private String getMostFrequentFromStream(Stream<String> stream) {
        return stream.collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }


    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    public Optional<AppUser> getUserById(Long id) {
        return appUserRepository.findById(id);
    }

    @Transactional
    public AppUser saveUser(AppUser user) {
        if (appUserRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalStateException("Username already taken.");
        }
        return appUserRepository.save(user);
    }



    public void deleteUser(Long id) {
        appUserRepository.deleteById(id);
    }

    @Transactional
    public AppUser markMovieAsWatched(Long userId, Long movieId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        if (watchedMovieRepository.existsByUserAndMovie(user, movie)) {
            throw new IllegalStateException("Movie already marked as watched by this user.");
        }



        WatchedMovie watched = new WatchedMovie(user, movie, LocalDate.now());
        watchedMovieRepository.save(watched);

        user.getWatchedMovies().add(watched);
        return appUserRepository.save(user);
    }



    @Transactional
    public AppUser markMovieAsWatchedWithDate(Long userId, Long movieId, LocalDate watchedDate) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        if (watchedMovieRepository.existsByUserAndMovie(user, movie)) {
            throw new IllegalStateException("Movie already marked as watched by this user.");
        }

        WatchedMovie watched = new WatchedMovie(user, movie, watchedDate);
        watchedMovieRepository.save(watched);

        user.getWatchedMovies().add(watched);
        return appUserRepository.save(user);
    }

    @Transactional
    public AppUser rateMovie(Long userId, Long movieId, int rating) {
        if (rating < 1 || rating > 10) {
            throw new IllegalArgumentException("Rating must be between 1 and 10");
        }

        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NoSuchElementException("Movie not found with id: " + movieId));

        Optional<WatchedMovie> watchedMovieOpt = user.getWatchedMovies().stream()
                .filter(wm -> wm.getMovie().equals(movie))
                .findFirst();

        if (watchedMovieOpt.isEmpty()) {
            throw new IllegalStateException("You must watch the movie before rating it.");
        }

        WatchedMovie watchedMovie = watchedMovieOpt.get();
        watchedMovie.setRating(rating);
        watchedMovieRepository.save(watchedMovie);

        return appUserRepository.save(user);
    }



    public String getFavoriteGenre(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        return getMostFrequentFromStream(
                user.getWatchedMovies().stream()
                        .map(wm -> wm.getMovie().getGenre().getName())
        );
    }


    public String getMostWatchedDirector(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();

        return user.getWatchedMovies().stream()
                .map(wm -> wm.getMovie().getDirector().getFullName())
                .collect(Collectors.groupingBy(name -> name, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }

    public double getAverageUserRating(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        return user.getWatchedMovies().stream()
                .map(WatchedMovie::getRating)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }


    public long getWatchedMoviesCountInLastDays(Long userId, int days) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        LocalDate now = LocalDate.now();
        LocalDate fromDate = now.minusDays(days);

        return user.getWatchedMovies().stream()
                .filter(wm -> wm.getWatchedDate() != null &&
                        (!wm.getWatchedDate().isBefore(fromDate) && !wm.getWatchedDate().isAfter(now)))
                .count();
    }

    public long getWatchedMoviesCountWeekly(Long userId) {
        return getWatchedMoviesCountInLastDays(userId, 7);
    }

    public long getWatchedMoviesCountMonthly(Long userId) {
        return getWatchedMoviesCountInLastDays(userId, 30);
    }

    public double getAverageRatingInLastDays(Long userId, int days) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        LocalDate now = LocalDate.now();
        LocalDate fromDate = now.minusDays(days);

        return user.getWatchedMovies().stream()
                .filter(wm -> wm.getWatchedDate() != null &&
                        (!wm.getWatchedDate().isBefore(fromDate) && !wm.getWatchedDate().isAfter(now)))
                .map(WatchedMovie::getRating)
                .filter(Objects::nonNull)
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
    }


    public double getAverageRatingWeekly(Long userId) {
        return getAverageRatingInLastDays(userId, 7);
    }

    public double getAverageRatingMonthly(Long userId) {
        return getAverageRatingInLastDays(userId, 30);
    }

    public Map<LocalDate, Long> getWatchedActivityInLastDays(Long userId, int days) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        LocalDate today = LocalDate.now();
        LocalDate fromDate = today.minusDays(days - 1);

        Map<LocalDate, Long> activity = fromDate.datesUntil(today.plusDays(1))
                .collect(Collectors.toMap(d -> d, d -> 0L));

        user.getWatchedMovies().stream()
                .filter(wm -> wm.getWatchedDate() != null &&
                        (!wm.getWatchedDate().isBefore(fromDate) && !wm.getWatchedDate().isAfter(today)))
                .forEach(wm -> activity.computeIfPresent(wm.getWatchedDate(), (d, count) -> count + 1));

        return activity;
    }

    public Map<Integer, Long> getRatingHistogram(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        Map<Integer, Long> histogram = new HashMap<>();
        for (int i = 1; i <= 10; i++) {
            histogram.put(i, 0L);
        }

        user.getWatchedMovies().stream()
                .map(WatchedMovie::getRating)
                .filter(Objects::nonNull)
                .forEach(rating -> histogram.computeIfPresent(rating, (key, value) -> value + 1));

        return histogram;
    }


    public List<Movie> getTopRatedMovies(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        return user.getWatchedMovies().stream()
                .filter(wm -> wm.getRating() != null)
                .sorted((w1, w2) -> Integer.compare(w2.getRating(), w1.getRating()))
                .limit(3)
                .map(WatchedMovie::getMovie)
                .toList();
    }


    public String getFavoriteWatchDay(Long userId) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));

        return user.getWatchedMovies().stream()
                .filter(wm -> wm.getWatchedDate() != null)
                .map(wm -> wm.getWatchedDate().getDayOfWeek().toString())
                .collect(Collectors.groupingBy(day -> day, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("No data");
    }

    public String getMostWatchedGenre(Long userId) {
        AppUser user = appUserRepository.findById(userId).orElseThrow();
        return getMostFrequentFromStream(
                user.getWatchedMovies().stream()
                        .map(wm -> wm.getMovie().getGenre().getName())
        );
    }

    @Transactional
    public AppUser updateUser(Long id, AppUser updatedUser) {
        AppUser existing = appUserRepository.findById(id).orElseThrow();
        existing.setUsername(updatedUser.getUsername());
        return appUserRepository.save(existing);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository.findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER")
                .build();
    }
}
