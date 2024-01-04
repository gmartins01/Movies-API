package com.goncalo.movies.services;

import com.goncalo.movies.dto.MovieDTO;
import com.goncalo.movies.entities.Movie;
import com.goncalo.movies.repositories.MovieRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Autowired
    @InjectMocks
    private MovieService movieService;



    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test with valid data")
    void validateMovieCase1() {
        Movie movie = new Movie(1L,"Test", LocalDate.of(2022,1,1),5,2222.22);

        assertTrue(movieService.validateMovie(movie));
    }



    @Test
    @DisplayName("Test to get validation errors")
    void validateMovieCase2() {
        Movie movie = new Movie(1L,null, LocalDate.of(2023,1,1),5,2222.22);

        assertThrows(ValidationException.class, () -> movieService.validateMovie(movie));
    }


    @Test
    @DisplayName("Test to create a movie")
    void createMovieCase1() {
        MovieDTO movieDTO = new MovieDTO("Test", LocalDate.of(2023,1,1),5,2222.22);

        Movie createdMovie = movieService.createMovie(movieDTO);

        assertNotNull(createdMovie);
        assertEquals(movieDTO.title(), createdMovie.getTitle());
        assertEquals(movieDTO.launchDate(), createdMovie.getLaunchDate());
        assertEquals(movieDTO.rank(), createdMovie.getRank());
        assertEquals(movieDTO.revenue(), createdMovie.getRevenue());
    }

    @Test
    @DisplayName("Test to create a movie with invalid data")
    void createMovieCase2() {
        MovieDTO movieDTO = new MovieDTO(null, LocalDate.of(3023,1,1),5,2222.22);

        assertThrows(ValidationException.class, () -> movieService.createMovie(movieDTO));
    }


    @Test
    @DisplayName("Test to get a movie by its Id")
    void getMovieByIdCase1(){
        Movie mockMovie = new Movie();
        Long validId = 1L;

        when(movieRepository.findById(validId)).thenReturn(Optional.of(mockMovie));

        Movie result = movieService.getMovieById(validId);

        assertEquals(mockMovie,result);
    }

    @Test
    @DisplayName("Test to get a movie with a invalid Id")
    void getMovieByIdCase2(){
        Long invalidId = 1L;

        when(movieRepository.findById(invalidId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> movieService.getMovieById(invalidId));

    }

    @Test
    @DisplayName("Test for updating a movie")
    void updateMovieCase1(){
        Long movieId = 1L;

        Movie existingMovie = new Movie(movieId, "Original Movie", LocalDate.of(2022, 5, 1), 8, 670.00);
        MovieDTO updatedMovieDTO = new MovieDTO("Updated Movie", LocalDate.of(2021, 6, 15), 5, 10.00);

        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        Movie updatedMovie = movieService.updateMovie(movieId, updatedMovieDTO);

        verify(movieRepository, times(1)).save(updatedMovie);
    }

    @Test
    @DisplayName("Test for updating non-existent movie")
    void updateMovieCase2(){
        Long movieId = 1L;
        MovieDTO validMovieDTO = new MovieDTO("Updated Movie", LocalDate.of(2003, 4, 16), 4, 47.00);

        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> movieService.updateMovie(movieId, validMovieDTO));

        verify(movieRepository, never()).save(any(Movie.class));
    }

    @Test
    @DisplayName("Test for delete a movie successfully")
    void deleteMovieCase1(){
        Long movieId = 1L;

        Movie existingMovie = new Movie();
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));

        movieService.deleteMovie(movieId);

        verify(movieRepository, times(1)).delete(existingMovie);
    }

    @Test
    @DisplayName("Test for delete a non-existent movie")
    void deleteMovieCase2(){
        Long movieId = 1L;
        when(movieRepository.findById(movieId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class,() -> movieService.deleteMovie(movieId));
    }

    @Test
    @DisplayName("Test for getting filtered movies")
    void getMoviesFilteredByLaunchDateCase1(){
        LocalDate startDate = LocalDate.of(2000, 2, 2);
        LocalDate endDate = LocalDate.of(2004, 10, 30);

        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(1L, "Spider-Man", LocalDate.of(2002, 7, 21),
                10,1000000000.00));
        movies.add(new Movie(2L, "Spider-Man 2", LocalDate.of(2004, 6, 30),
                9, 2000000000.50));

        when(movieRepository.findByLaunchDateBetween(startDate,endDate)).thenReturn(movies);

        List<Movie> filteredMovies = movieService.getMoviesFilteredByLaunchDate(startDate, endDate);

        assertEquals(movies.size(), filteredMovies.size());
    }

}