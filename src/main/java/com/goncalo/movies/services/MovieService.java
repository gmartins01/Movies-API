package com.goncalo.movies.services;

import com.goncalo.movies.repositories.MovieRepository;
import com.goncalo.movies.dto.MovieDTO;
import com.goncalo.movies.entities.Movie;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;


    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public Movie createMovie(MovieDTO data){
        Movie newMovie = new Movie(data);
        if (validateMovie(newMovie))
            movieRepository.save(newMovie);
        return newMovie;
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found: "+id));
    }

    public Movie updateMovie(Long movieId, MovieDTO data) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);

        return optionalMovie.map(movie -> {
            movie.setTitle(data.title());
            movie.setLaunchDate(data.launchDate());
            movie.setRank(data.rank());
            movie.setRevenue(data.revenue());

            if (validateMovie(movie)) {
                movieRepository.save(movie);
            }
            return movie;
        }).orElseThrow(() -> new EntityNotFoundException("Id not found: " + movieId));
    }

    public void deleteMovie(Long id){
        Optional<Movie> optionalMovie = movieRepository.findById(id);

        if(optionalMovie.isPresent()){
            Movie movie = optionalMovie.get();

            movieRepository.delete(movie);

        }else{
            throw new EntityNotFoundException("Id not found: "+id);
        }
    }

    public List<Movie> getMoviesFilteredByLaunchDate(LocalDate startDate, LocalDate endDate) {
        return movieRepository.findByLaunchDateBetween(startDate, endDate);
    }


    protected Boolean validateMovie(@NotNull Object object) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        if (!violations.isEmpty()) {

            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                errorMessage.append(violation.getMessage()).append(";");
            }
            throw new ValidationException(errorMessage.toString());
        }

        return true;
    }


}
