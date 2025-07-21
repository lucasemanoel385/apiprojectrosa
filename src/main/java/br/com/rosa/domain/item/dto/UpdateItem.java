package br.com.rosa.domain.item.dto;

public record UpdateItem(
		Long cod,
		String reference,
		String name,
		String url,
		double value,
		String replacementValue,
		Long amount,
		String category		) {

}
