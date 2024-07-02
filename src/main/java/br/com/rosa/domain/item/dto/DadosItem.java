package br.com.rosa.domain.item.dto;


import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.item.Item;

public record DadosItem(
		Long id ,
		Long cod,
		String name,
		double value,
		double replacementValue,
		Long amount,
		Category category,
		String imagem) {
	
	public DadosItem(Item item, String imagem) {
		this(item.getId() ,item.getCod(), item.getName(),
				item.getValueItem(), item.getReplacementValue(),
				item.getAmount(), item.getCategory(), imagem);
		
	}

}
