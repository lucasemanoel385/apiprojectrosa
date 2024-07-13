package br.com.rosa.domain.expenses.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DataExepenses(
        @NotNull
        String description,
        @NotNull
        double value,
        @NotNull(message = "Data não deve ser nulo")
        LocalDate date
        ) {
}
