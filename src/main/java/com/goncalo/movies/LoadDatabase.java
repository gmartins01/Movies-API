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
                    new Movie(1L,"Teste", LocalDate.of(2023,1,1),4,44.44),
                    new Movie(2L,"Teste1", LocalDate.of(2021,11,1),5,44.44),
                    new Movie(3L,"Teste2", LocalDate.of(2022,12,13),7,44.44)
            );

            movieRepository.saveAll(movies);
        };
    }
}
