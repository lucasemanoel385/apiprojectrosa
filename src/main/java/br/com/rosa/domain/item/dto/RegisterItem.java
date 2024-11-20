package br.com.rosa.domain.item.dto;



public record RegisterItem(
		Long cod,
		String name,
		double replacementValue,
		int amount,
		String category
		) {

}
