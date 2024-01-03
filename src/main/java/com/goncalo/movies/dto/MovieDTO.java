package com.goncalo.movies.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record MovieDTO(
        String title,
        LocalDate launchDate,
        int rank,
        double revenue) {
}
