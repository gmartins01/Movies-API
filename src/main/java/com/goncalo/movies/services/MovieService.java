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
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;

@Service
public class MovieService {
    @Autowired
    private MovieRepository movieRepository;

    protected Boolean validateMovie(@NotNull Movie movie) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Movie>> violations = validator.validate(movie);
        if (!violations.isEmpty()) {

            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Movie> violation : violations) {
                errorMessage.append(violation.getMessage()).append(";");
            }
            throw new ValidationException(errorMessage.toString());
        }

        return true;
    }

    private <T> void updateIfNotNull(T value, Consumer<T> updateFunction) {
        if (Objects.nonNull(value)) {
            updateFunction.accept(value);
        }
    }

    private void updateIfNotZero(int value, IntConsumer updateFunction) {
        if (value != 0) {
            updateFunction.accept(value);
        }
    }

    private void updateIfNotZero(double value, DoubleConsumer updateFunction) {
        if (value != 0.0) {
            updateFunction.accept(value);
        }
    }

    public List<Movie> getAllMovies(){
        return this.movieRepository.findAll();
    }

    public Movie createMovie(MovieDTO data){
        Movie newMovie = new Movie(data);
        if (validateMovie(newMovie))
            this.movieRepository.save(newMovie);
        return newMovie;
    }

    public Movie getMovieById(Long id) {
        return this.movieRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Id not found: "+id));
    }

    public Movie updateMovie(Long movieId, MovieDTO data) {
        Optional<Movie> optionalMovie = this.movieRepository.findById(movieId);

        return optionalMovie.map(movie -> {
            updateIfNotNull(data.title(), movie::setTitle);
            updateIfNotNull(data.launchDate(), movie::setLaunchDate);
            updateIfNotZero(data.rank(), movie::setRank);
            updateIfNotZero(data.revenue(), movie::setRevenue);

            if (validateMovie(movie)) {
                this.movieRepository.save(movie);
            }
            return movie;
        }).orElseThrow(() -> new EntityNotFoundException("Id not found: " + movieId));
    }

    public void deleteMovie(Long id){
        Optional<Movie> optionalMovie = this.movieRepository.findById(id);

        if(optionalMovie.isPresent()){
            Movie movie = optionalMovie.get();

            this.movieRepository.delete(movie);

        }else{
            throw new EntityNotFoundException("Id not found: "+id);
        }
    }

    public List<Movie> getMoviesFilteredByLaunchDate(LocalDate startDate, LocalDate endDate) {
        return this.movieRepository.findByLaunchDateBetween(startDate, endDate);
    }

}
