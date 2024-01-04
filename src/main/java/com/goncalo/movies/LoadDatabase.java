package com.goncalo.movies;

import com.goncalo.movies.entities.Movie;
import com.goncalo.movies.repositories.MovieRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class LoadDatabase {

    private static  final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    public ApplicationRunner initDatabase(MovieRepository movieRepository){
        return args -> {
            List<Movie> movies = List.of(
                    new Movie(1L,"Spider-Man", LocalDate.of(2023,7,1),10,8484884844.44),
                    new Movie(2L,"Spider-Man 2", LocalDate.of(2004,6,30),9,84848848440.44),
                    new Movie(3L,"Spider-Man 3", LocalDate.of(2022,12,13),8,8484884844.44)
            );

            movieRepository.saveAll(movies);
        };
    }
}
