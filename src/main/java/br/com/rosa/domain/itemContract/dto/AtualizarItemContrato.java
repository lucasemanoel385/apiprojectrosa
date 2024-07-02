package br.com.rosa.domain.itemContract.dto;

public record AtualizarItemContrato(
		Long id,
		String nome,
		Float valor,
		int quantidade,
		Long categoria
		) {

}
