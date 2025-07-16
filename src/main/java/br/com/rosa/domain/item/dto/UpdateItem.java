package br.com.rosa.domain.item.dto;

public record UpdateItem(
		Long cod,
		String reference,
		String name,
		double value,
		String replacementValue,
		Long amount,
		String category		) {

}
