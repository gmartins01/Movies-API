package com.goncalo.movies.controllers;

import com.goncalo.movies.entities.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createMovie(){
        Movie movie = new Movie(1L,"Test", LocalDate.of(2022,2,2),4,26.44);

        Movie createdMovie = restTemplate.postForObject("/movies",movie,Movie.class);

        assertNotNull(createdMovie);
        assertEquals(createdMovie,movie);
    }
}