package com.example.moviecollection;

import com.example.moviecollection.model.*;
import com.example.moviecollection.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DataInitializer implements CommandLineRunner {

    private final GenreRepository genreRepo;
    private final DirectorRepository directorRepo;
    private final MovieRepository movieRepo;
    private final AppUserRepository userRepo;

    public DataInitializer(
            GenreRepository genreRepo,
            DirectorRepository directorRepo,
            MovieRepository movieRepo,
            AppUserRepository userRepo
    ) {
        this.genreRepo = genreRepo;
        this.directorRepo = directorRepo;
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        Genre drama = genreRepo.save(new Genre(null, "Drama"));
        Genre scifi = genreRepo.save(new Genre(null, "Sci-Fi"));
        Genre thriller = genreRepo.save(new Genre(null, "Thriller"));

        Director nolan = directorRepo.save(new Director(null, "Christopher Nolan"));
        Director villeneuve = directorRepo.save(new Director(null, "Denis Villeneuve"));
        Director fincher = directorRepo.save(new Director(null, "David Fincher"));

        Movie m1 = movieRepo.save(new Movie(null, "Inception", "Dreams inside dreams", 2010, nolan, scifi));
        Movie m2 = movieRepo.save(new Movie(null, "Interstellar", "Space and time", 2014, nolan, scifi));
        Movie m3 = movieRepo.save(new Movie(null, "Arrival", "Linguistics meets aliens", 2016, villeneuve, drama));
        Movie m4 = movieRepo.save(new Movie(null, "The Social Network", "Facebook story", 2010, fincher, drama));
        Movie m5 = movieRepo.save(new Movie(null, "Se7en", "Deadly sins and serial killer", 1995, fincher, thriller));

        // uzytkownik 1 z obejrzanymi filmami i ocenami
        AppUser user1 = new AppUser();
        user1.setUsername("testuser");
        user1.setWatchedMovies(List.of(m1, m3, m5));
        user1.setRatings(Map.of(
                m1, 9,
                m3, 8,
                m5, 10
        ));
        userRepo.save(user1);

        // uzytkownik 2 bez obejrzanych filmow
        AppUser user2 = new AppUser();
        user2.setUsername("emptyuser");
        user2.setWatchedMovies(List.of()); // pusty koszyk
        userRepo.save(user2);
    }
}
