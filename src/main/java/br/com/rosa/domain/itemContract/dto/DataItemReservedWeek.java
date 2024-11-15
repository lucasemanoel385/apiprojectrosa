package br.com.rosa.domain.itemContract.dto;

import br.com.rosa.domain.itemContract.ItemContract;

import java.time.LocalDate;

public record DataItemReservedWeek(
        Long cod,
        String name,
        Long amount,
        LocalDate finalDate
) {
    public DataItemReservedWeek(ItemContract reserved) {
        this(reserved.getCod(), reserved.getName(), reserved.getQuantity(), reserved.getFinalDate());
    }
}
