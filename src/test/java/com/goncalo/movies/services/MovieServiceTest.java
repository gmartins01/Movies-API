package com.goncalo.movies.services;

import com.goncalo.movies.dto.MovieDTO;
import com.goncalo.movies.entities.Movie;
import com.goncalo.movies.repositories.MovieRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class MovieServiceTest {

    private Validator validator;

    @Mock
    private MovieRepository movieRepository;

    @Autowired
    @InjectMocks
    private MovieService movieService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create a movie successfully")
    void createMovieCase1() {
        MovieDTO movieDTO = new MovieDTO("Teste", LocalDate.of(2023,1,1),5,2222.2);

        Movie createdMovie = this.movieService.createMovie(movieDTO);

        assertNotNull(createdMovie);
        assertEquals(movieDTO.title(), createdMovie.getTitle());
        assertEquals(movieDTO.launchDate(), createdMovie.getLaunchDate());
        assertEquals(movieDTO.rank(), createdMovie.getRank());
        assertEquals(movieDTO.revenue(), createdMovie.getRevenue());
    }

    @Test
    @DisplayName("Should get validation error - title null")
    void createMovieCase2() {
        MovieDTO movieDTO = new MovieDTO(null, LocalDate.of(2023,1,1),5,2222.2);

        Set<ConstraintViolation<MovieDTO>> violations = validator.validate(movieDTO);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should get validation error - date from the future")
    void createMovieCase3() {
        MovieDTO movieDTO = new MovieDTO("2023", LocalDate.of(3023,1,1),5,2222.2);

        Set<ConstraintViolation<MovieDTO>> violations = validator.validate(movieDTO);

        assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Should not update movie")
    void updateMovieCase2(){


    }

}