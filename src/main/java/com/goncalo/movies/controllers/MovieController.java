package com.goncalo.movies.controllers;

import com.goncalo.movies.dto.MovieDTO;
import com.goncalo.movies.entities.Movie;
import com.goncalo.movies.services.MovieService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/movies")
public class MovieController {
    @Autowired
    private MovieService movieService;

    @PostMapping
    private ResponseEntity<Movie> createMovie(@RequestBody @Valid MovieDTO movie){
        Movie newMovie = this.movieService.createMovie(movie);
        return new ResponseEntity<>(newMovie, HttpStatus.CREATED);
    }

    @GetMapping
    private ResponseEntity<List<Movie>> getAllMovies(){
        List<Movie> movies = this.movieService.getAllMovies();
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(movies,HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        Movie movie = this.movieService.getMovieById(id);
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    private ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody @Valid MovieDTO data){
        Movie updatedMovie = this.movieService.updateMovie(id,data);
        return new ResponseEntity<>(updatedMovie,HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteMovie(@PathVariable Long id){
        this.movieService.deleteMovie(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Movie>> getMoviesByLaunchDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<Movie> filteredMovies = this.movieService.getMoviesFilteredByLaunchDate(startDate, endDate);
        if(filteredMovies.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return new ResponseEntity<>(filteredMovies, HttpStatus.OK);
    }
}
