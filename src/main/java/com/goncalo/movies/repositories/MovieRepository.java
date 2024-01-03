package com.goncalo.movies.repositories;

import com.goncalo.movies.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie,Long> {
    List<Movie> findByLaunchDateBetween(LocalDate startDate, LocalDate endDate);
}
