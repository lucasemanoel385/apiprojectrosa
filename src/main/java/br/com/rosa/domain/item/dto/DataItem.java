package br.com.rosa.domain.item.dto;


import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.itemContract.ItemContract;

public record DataItem(
		Long cod,
		String reference,
		String name,
		String url,
		String replacementValue,
		Long amount,
		Category category) {
	
	public DataItem(Item item) {
		this(item.getCod(), item.getReference(),item.getName(), item.getUrl(),
				item.getReplacementValue(),
				item.getQuantity(), item.getCategory());
		
	}
}
