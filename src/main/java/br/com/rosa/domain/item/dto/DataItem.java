package br.com.rosa.domain.item.dto;


import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.itemContract.ItemContract;

public record DataItem(
		Long cod,
		String reference,
		String name,
		double replacementValue,
		Long amount,
		Category category,
		String imagem) {
	
	public DataItem(Item item, String imagem) {
		this(item.getCod(), item.getReference(),item.getName(),
				item.getReplacementValue(),
				item.getQuantity(), item.getCategory(), imagem);
		
	}
}
