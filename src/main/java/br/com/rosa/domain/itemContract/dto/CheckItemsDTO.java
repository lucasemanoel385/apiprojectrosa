package br.com.rosa.domain.itemContract.dto;

import java.time.LocalDate;

public record CheckItemsDTO(
        String search,
        LocalDate dateStart,
        LocalDate dateFinal
) {
}
