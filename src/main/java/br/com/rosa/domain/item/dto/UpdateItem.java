package br.com.rosa.domain.item.dto;

public record UpdateItem(
		Long id,
		Long cod,
		String name,
		double value,
		double replacementValue,
		Long amount,
		String category		) {

}