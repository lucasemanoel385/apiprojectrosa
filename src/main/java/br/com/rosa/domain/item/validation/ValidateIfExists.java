package br.com.rosa.domain.item.validation;

import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.item.dto.UpdateItem;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateIfExists  {
	
	@Autowired
	private RepositoryItem repository;

	@Autowired
	private RepositoryCategory repositoryCategory;

	public void validateRegisterItem(Long codItem, String nameCategory, String nameItem) {

		if (codItem != 0) {
			if (repository.existsByCod(codItem)) {
				throw new SqlConstraintViolationException("Código do produto já existe");
			}
		}

		if (!repositoryCategory.existsByName(nameCategory)) {
			throw new SqlConstraintViolationException("Categoria não foi criada");
		}

		if (repository.existsByName(nameItem)) {
			throw new SqlConstraintViolationException("Nome do produto já cadastrado");
		}
	}

	public void validateUpdateItem(UpdateItem data, Item item) {

		if (data.cod() != 0) {
			if (!item.getCod().equals(data.cod()) && repository.existsByCod(data.cod())) {
				throw new SqlConstraintViolationException("Código do produto já existe");
			}
		}

		if (!repositoryCategory.existsByName(data.category())) {
			throw new SqlConstraintViolationException("Categoria não foi criada");
		}

		if (!data.name().equals(item.getName()) && repository.existsByName(data.name())) {
			throw new SqlConstraintViolationException("Nome do produto já cadastrado");
		}

	}



}
