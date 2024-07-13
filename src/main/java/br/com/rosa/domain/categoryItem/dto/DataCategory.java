package br.com.rosa.domain.categoryItem.dto;

import br.com.rosa.domain.categoryItem.Category;

public record DataCategory(Long id, String category) {
	
	public DataCategory(Category categoria) {

		this(categoria.getId(),categoria.getName());
	}

}
