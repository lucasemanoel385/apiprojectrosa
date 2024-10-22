package br.com.rosa.domain.itemContract.dto;

import br.com.rosa.domain.itemContract.ItemContract;

import java.time.LocalDate;
import java.util.List;

public record DataItemReservedWeek(
        Long id,
        Long cod,
        String name,
        Long amount,
        LocalDate finalDate
) {
    public DataItemReservedWeek(ItemContract reserved) {
        this(reserved.getIdItem(), reserved.getCod(), reserved.getName(), reserved.getAmount(), reserved.getFinalDate());
    }
}
