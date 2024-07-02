package br.com.rosa.domain.contract.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record UpdateContract(
		@NotNull
		Long contractId,
		Long clientId,
		String contactPhone,
		LocalDate dateOf,
		LocalDate dateUntil,
		List<ContratoItem> items,
		double discount,
		String observation,
		String annotations
		) {

}
