package com.goncalo.movies.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goncalo.movies.dto.MovieDTO;
import com.goncalo.movies.entities.Movie;
import com.goncalo.movies.services.MovieService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@WebMvcTest(controllers = MovieController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class MovieControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Test for creating a movie via POST request")
    void createMovie() throws Exception{
        Long movieId = 1L;
        LocalDate date = LocalDate.of(2022,1,1);

        MovieDTO movieDTO = new MovieDTO("Test", date,5,2222.22);
        Movie newMovie = new Movie(movieDTO);
        newMovie.setId(movieId);

        given(movieService.createMovie(ArgumentMatchers.any())).willReturn(newMovie);

        ResultActions response = mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieDTO)));

        // Assert launchDate because of the format
        String jsonResponse = response.andReturn().getResponse().getContentAsString();
        Movie movieResponse = objectMapper.readValue(jsonResponse, Movie.class);
        assertEquals(date, movieResponse.getLaunchDate());

        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movieId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(movieDTO.title())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank", CoreMatchers.is(movieDTO.rank())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.revenue", CoreMatchers.is(movieDTO.revenue())));
    }

    @Test
    @DisplayName("Test to get all movies via GET request")
    void getAllMoviesCase1() throws Exception {
        List<Movie> movies = new ArrayList<>();
        movies.add(new Movie(1L,"Test 1", LocalDate.of(2022,1,1), 4, 12.00));
        movies.add(new Movie(2L,"Test 2", LocalDate.of(2022,1,1), 4, 12.00));

        when(movieService.getAllMovies()).thenReturn(movies);

        ResultActions response = mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()",CoreMatchers.is(movies.size())));
    }

    @Test
    @DisplayName("Test to get all movies via GET request when there is no movies")
    void getAllMoviesCase2() throws Exception {
        when(movieService.getAllMovies()).thenReturn(Collections.emptyList());

        ResultActions response = mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("Test to get movie by ID via GET request")
    void getMovieById() throws Exception {
        Long movieId = 1L;
        LocalDate date = LocalDate.of(2022,1,1);
        Movie movie = new Movie(movieId,"Test 1",date, 4, 12.00);


        when(movieService.getMovieById(movieId)).thenReturn(movie);

        ResultActions response = mockMvc.perform(get("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON));

        String jsonResponse = response.andReturn().getResponse().getContentAsString();
        Movie movieResponse = objectMapper.readValue(jsonResponse, Movie.class);
        assertEquals(date, movieResponse.getLaunchDate());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movieId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(movie.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank", CoreMatchers.is(movie.getRank())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.revenue", CoreMatchers.is(movie.getRevenue())));
    }

    @Test
    @DisplayName("Test to update a movie via PUT request")
    void updateMovie() throws Exception {
        Long movieId = 1L;
        LocalDate date = LocalDate.of(2022, 1, 1);

        MovieDTO movie = new MovieDTO("Movie Original", date, 8, 12.99);

        Movie expectedUpdatedMovie = new Movie(movieId, "Movie Updated", date, 2, 12.00);

        when(movieService.updateMovie(movieId, movie)).thenReturn(expectedUpdatedMovie);

        ResultActions response = mockMvc.perform(put("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)));

        String jsonResponse = response.andReturn().getResponse().getContentAsString();
        Movie movieResponse = objectMapper.readValue(jsonResponse, Movie.class);

        assertEquals(expectedUpdatedMovie.getLaunchDate(), movieResponse.getLaunchDate());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(movieId.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is(expectedUpdatedMovie.getTitle())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank", CoreMatchers.is(expectedUpdatedMovie.getRank())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.revenue", CoreMatchers.is(expectedUpdatedMovie.getRevenue())));
    }


    @Test
    @DisplayName("Test to update a delete via DELETE request")
    void deleteMovie() throws Exception {
        Long movieId = 1L;

        doNothing().when(movieService).deleteMovie(movieId);

        ResultActions response = mockMvc.perform(delete("/api/movies/1")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Test to get movies filtered by launch date via GET request")
    void getMoviesByLaunchDateRangeCase1() throws Exception {
        LocalDate startDate = LocalDate.of(2003, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        List<Movie> filteredMovies = new ArrayList<>();

        filteredMovies.add(new Movie(1L, "Movie 1", LocalDate.of(2002, 2, 2),
                1, 1.0));
        filteredMovies.add(new Movie(2L, "Movie 2", LocalDate.of(2022, 2, 2),
                2,2.0));

        when(movieService.getMoviesFilteredByLaunchDate(startDate, endDate)).thenReturn(filteredMovies);

        ResultActions response = mockMvc.perform(get("/api/movies/filter?startDate=2003-01-01&endDate=2024-12-31")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()",CoreMatchers.is(filteredMovies.size())));
    }

    @Test
    @DisplayName("Test to get movies filtered by launch date when there is no movies via GET request")
    void getMoviesByLaunchDateRangeCase2() throws Exception {
        LocalDate startDate = LocalDate.of(2003, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);

        when(movieService.getMoviesFilteredByLaunchDate(startDate, endDate)).thenReturn(Collections.emptyList());

        ResultActions response = mockMvc.perform(get("/api/movies/filter?startDate=2003-01-01&endDate=2024-12-31")
                .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
