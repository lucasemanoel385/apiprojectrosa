package br.com.rosa.domain.contract.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public record ContractRegister(
		Long client,
		@FutureOrPresent(message = "Deve ser uma data presente ou futura")
		LocalDate dateOf,
		@FutureOrPresent(message = "Deve ser uma data presente ou futura")
		LocalDate dateUntil,
		double discount,
		String seller,
		@NotNull(message = "Não é possivel ter 0 itens")
		List<ContractItem> items,
		String observation,
		String annotations
		) {

}
