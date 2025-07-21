package br.com.rosa.domain.itemContract.dto;

import br.com.rosa.domain.contract.enunm.SituationContract;

import java.time.LocalDate;

public record ItemsAvailableDTO(
        Long cod,
        String reference,
        String name,
        Long quantityAvailable,
        SituationContract status,
        LocalDate dateStart,
        LocalDate dateFinal,
        Long contractId,
        String nameClient) {

    public ItemsAvailableDTO(ItemsWithContractIdDTO item, Long check) {
        this(item.getCod(), item.getReference(), item.getName(),
                check, SituationContract.valueOf(item.getContractSituation()), item.getStartDate(), item.getFinalDate(), item.getContractId(), item.getNameClient());

    }
}
