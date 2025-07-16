package br.com.rosa.domain.itemContract.dto;

import java.time.LocalDate;

public interface ItemsWithContractIdDTO {
    Long getId();
    Long getCod();
    String getReference();
    String getName();
    Integer getQuantity();
    LocalDate getStartDate();
    LocalDate getFinalDate();
    String getContractSituation();
    Long getContractId();
    String getNameClient();
}
