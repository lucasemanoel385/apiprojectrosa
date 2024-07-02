package br.com.rosa.domain.itemContract.dto;

import br.com.rosa.domain.item.Item;

public record DadosItemContrato(String nome) {
	
	public DadosItemContrato(Item item) {
		this(item.getName());
	}
	
}
