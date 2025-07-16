package br.com.rosa.domain.itemContract.dto;

import br.com.rosa.domain.itemContract.ItemContract;

import java.time.LocalDate;

public record DataItemReservedWeek(
        Long cod,
        String reference,
        String name,
        Long amount,
        LocalDate startDate,
        LocalDate finalDate
) {
    public DataItemReservedWeek(ItemContract reserved) {
        this(reserved.getCod(), reserved.getReference(),reserved.getName(), reserved.getQuantity(), reserved.getStartDate(), reserved.getFinalDate());
    }
}
