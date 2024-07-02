package br.com.rosa.domain.item.dto;



public record ItemCadastro(
		Long cod,
		String name,
		double value,
		double replacementValue,
		int amount,
		String category
		) {

}
