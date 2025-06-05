package com.example.moviecollection;

import com.example.moviecollection.model.*;
import com.example.moviecollection.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final GenreRepository genreRepo;
    private final DirectorRepository directorRepo;
    private final MovieRepository movieRepo;
    private final AppUserRepository userRepo;
    private final WatchedMovieRepository watchedRepo;

    public DataInitializer(
            GenreRepository genreRepo,
            DirectorRepository directorRepo,
            MovieRepository movieRepo,
            AppUserRepository userRepo,
            WatchedMovieRepository watchedRepo
    ) {
        this.genreRepo = genreRepo;
        this.directorRepo = directorRepo;
        this.movieRepo = movieRepo;
        this.userRepo = userRepo;
        this.watchedRepo = watchedRepo;
    }

    @Override
    public void run(String... args) throws Exception {

        Genre drama = genreRepo.save(new Genre(null, "Drama"));
        Genre scifi = genreRepo.save(new Genre(null, "Sci-Fi"));
        Genre thriller = genreRepo.save(new Genre(null, "Thriller"));
        Genre action = genreRepo.save(new Genre(null, "Action"));
        Genre comedy = genreRepo.save(new Genre(null, "Comedy"));
        Genre horror = genreRepo.save(new Genre(null, "Horror"));
        Genre fantasy = genreRepo.save(new Genre(null, "Fantasy"));

        Director nolan = directorRepo.save(new Director(null, "Christopher Nolan"));
        Director villeneuve = directorRepo.save(new Director(null, "Denis Villeneuve"));
        Director fincher = directorRepo.save(new Director(null, "David Fincher"));
        Director columbus = directorRepo.save(new Director(null, "Chris Columbus"));
        Director cuaron = directorRepo.save(new Director(null, "Alfonso Cuarón"));
        Director newell = directorRepo.save(new Director(null, "Mike Newell"));
        Director yates = directorRepo.save(new Director(null, "David Yates"));

        Movie m1 = movieRepo.save(new Movie(null, "Inception", "Dreams inside dreams", 2010, nolan, scifi));
        Movie m2 = movieRepo.save(new Movie(null, "Interstellar", "Space and time", 2014, nolan, scifi));
        Movie m3 = movieRepo.save(new Movie(null, "Arrival", "Linguistics meets aliens", 2016, villeneuve, drama));
        Movie m4 = movieRepo.save(new Movie(null, "The Social Network", "Facebook story", 2010, fincher, drama));
        Movie m5 = movieRepo.save(new Movie(null, "Se7en", "Deadly sins and serial killer", 1995, fincher, thriller));
        Movie m6 = movieRepo.save(new Movie(null, "Harry Potter and the Sorcerer's Stone", "An orphaned boy discovers on his 11th birthday that he is a wizard and begins his education at Hogwarts School of Witchcraft and Wizardry.", 2001, columbus, fantasy));
        Movie m7 = movieRepo.save(new Movie(null, "Harry Potter and the Chamber of Secrets", "Harry returns to Hogwarts and finds the school plagued by mysterious attacks that leave students petrified.", 2002, columbus, fantasy));
        Movie m8 = movieRepo.save(new Movie(null, "Harry Potter and the Prisoner of Azkaban", "Harry learns that Sirius Black, an escaped prisoner believed to have betrayed his parents, is hunting him.", 2004, cuaron, fantasy));
        Movie m9 = movieRepo.save(new Movie(null, "Harry Potter and the Goblet of Fire", "Harry is unexpectedly entered into the dangerous Triwizard Tournament.", 2005, newell, fantasy));
        Movie m10 = movieRepo.save(new Movie(null, "Harry Potter and the Order of the Phoenix", "With Voldemort’s return denied by the Ministry of Magic, Harry and his friends form “Dumbledore’s Army” to defend themselves.", 2007, yates, fantasy));
        Movie m11 = movieRepo.save(new Movie(null, "Harry Potter and the Half-Blood Prince", "As Voldemort tightens his grip on the wizarding and Muggle worlds, Harry finds a mysterious potions book belonging to the “Half-Blood Prince”.", 2009, yates, fantasy));
        Movie m12 = movieRepo.save(new Movie(null, "Harry Potter and the Deathly Hallows", "Harry, Ron, and Hermione leave Hogwarts to destroy Voldemort’s Horcruxes. As they face danger and doubt on their journey, their friendship is tested like never before.", 2010, yates, fantasy));
        Movie m13 = movieRepo.save(new Movie(null, "Harry Potter and the Deathly Hallows – Part 2", "The final battle at Hogwarts begins. Harry must face Voldemort in a climactic showdown as the fate of the wizarding world hangs in the balance.", 2011, yates, fantasy));

        AppUser user1 = new AppUser();
        user1.setUsername("testuser");
        user1.setPassword(new BCryptPasswordEncoder().encode("testpass"));
        user1 = userRepo.save(user1);

        WatchedMovie wm1 = new WatchedMovie(user1, m1, LocalDate.of(2024, 1, 10));
        wm1.setRating(9);
        WatchedMovie wm2 = new WatchedMovie(user1, m3, LocalDate.of(2024, 3, 15));
        wm2.setRating(8);
        WatchedMovie wm3 = new WatchedMovie(user1, m5, LocalDate.of(2024, 4, 2));
        wm3.setRating(10);

        watchedRepo.saveAll(List.of(wm1, wm2, wm3));

        user1.setWatchedMovies(List.of(wm1, wm2, wm3));
        userRepo.save(user1);

        AppUser user2 = new AppUser();
        user2.setUsername("emptyuser");
        user2.setPassword(new BCryptPasswordEncoder().encode("empty123"));
        user2.setWatchedMovies(List.of());
        userRepo.save(user2);
    }
}
