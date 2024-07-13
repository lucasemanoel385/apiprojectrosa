package br.com.rosa.domain.itemContract.dto;

public record UpdateItemContract(
		Long id,
		String nome,
		Float valor,
		int quantidade,
		Long categoria
		) {

}
