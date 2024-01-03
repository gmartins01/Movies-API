package com.goncalo.movies.entities;

import com.goncalo.movies.dto.MovieDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity(name="movies")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Size(min = 2, max = 250, message = "Title must be between {min} and {max} characters long")
    @NotBlank(message = "Title should not be blank or null")
    private String title;

    @Column(name = "launch_date", nullable = false)
    @PastOrPresent(message = "Date should not be in the future")
    private LocalDate launchDate;

    @Column(name = "rank", nullable = false)
    @Min(value = 0, message = "Rank must be greater than or equal to {value}")
    @Max(value = 10, message = "Rank must be less than or equal to {value}")
    private int rank;

    @Column(name = "revenue", nullable = false)
    private double revenue;

    public Movie(MovieDTO data){
        this.title = data.title();
        this.launchDate = data.launchDate();
        this.rank = data.rank();
        this.revenue = data.revenue();
    }
}
