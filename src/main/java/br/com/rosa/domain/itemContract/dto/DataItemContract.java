package br.com.rosa.domain.itemContract.dto;

import br.com.rosa.domain.item.Item;

public record DataItemContract(String nome) {
	
	public DataItemContract(Item item) {
		this(item.getName());
	}
	
}
