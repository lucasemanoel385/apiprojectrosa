package br.com.rosa.domain.itemContract.dto;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public record VerifyItemDTO(
        Long cod,
        int quantity,
        @FutureOrPresent
        LocalDate dateStart,
        @FutureOrPresent
        LocalDate dateFinal
) {
}
