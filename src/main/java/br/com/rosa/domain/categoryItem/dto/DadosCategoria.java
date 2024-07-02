package br.com.rosa.domain.categoryItem.dto;

import br.com.rosa.domain.categoryItem.Category;

public record DadosCategoria(Long id, String category) {
	
	public DadosCategoria(Category categoria) {

		this(categoria.getId(),categoria.getName());
	}

}
