package br.com.rosa.domain.contract.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record UpdateContract(
		@NotNull
		Long contractId,
		Long clientId,
		String contactPhone,
		LocalDate dateOf,
		@FutureOrPresent(message = "Deve ser uma data presente ou futura")
		LocalDate dateUntil,
		@FutureOrPresent(message = "Data prova deve ser uma data presente ou futura")
		LocalDate dateTrialDress,
		LocalDate dateEvent,
		@NotNull(message = "Não é possivel ter 0 itens")
		List<ContractItem> items,
		double discount,
		String seller,
		String observation,
		String annotations
		) {

}
