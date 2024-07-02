package br.com.rosa.domain.contract.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.rosa.domain.contract.enunm.SituacaoContrato;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;

public record ContratoCadastro(
		Long client,
		@FutureOrPresent(message = "Deve ser uma data presente ou futura")
		LocalDate dateOf,
		@FutureOrPresent(message = "Deve ser uma data presente ou futura")
		LocalDate dateUntil,
		double discount,
		String seller,
		List<ContratoItem> items,
		String observation,
		String annotations
		) {

}
