package br.com.rosa.domain.item.dto;



public record RegisterItem(
		Long cod,
		String reference,
		String name,
		String url,
		String replacementValue,
		int amount,
		String category
		) {

}
