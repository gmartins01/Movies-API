package com.goncalo.movies.repositories;

import com.goncalo.movies.dto.MovieDTO;
import com.goncalo.movies.entities.Movie;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static  org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MovieRepositoryTest {

    @Autowired
    MovieRepository movieRepository;

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Test to get movies filtered by launch date")
    void findByLaunchDateBetweenCase1() {
        MovieDTO movieDTO = new MovieDTO("Test", LocalDate.of(2023,1,1),5,2222.2);
        this.createMovie(movieDTO);

        Optional<List<Movie>> result = Optional.ofNullable(this.movieRepository.findByLaunchDateBetween(LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1)));

        assertThat(result.isPresent()).isTrue();

    }


    @Test
    @DisplayName("Test to get movies filtered by launch date when there is no movies")
    void findByLaunchDateBetweenCase2() {
        Optional<List<Movie>> result = Optional.ofNullable(this.movieRepository.findByLaunchDateBetween(LocalDate.of(2022, 1, 1), LocalDate.of(2024, 1, 1)));

        assertThat(result.isPresent()).isTrue();

    }

    private Movie createMovie(MovieDTO movieDTO){
        Movie newMovie = new Movie(movieDTO);
        this.entityManager.persist(newMovie);
        return newMovie;
    }



}