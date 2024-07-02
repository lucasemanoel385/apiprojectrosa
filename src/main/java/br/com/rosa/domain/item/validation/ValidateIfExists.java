package br.com.rosa.domain.item.validation;

import br.com.rosa.domain.categoryItem.RepositoryCategoria;
import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.RepositoryContrato;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;
import br.com.rosa.domain.contract.validations.ValidadorContratoAluguel;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.ItemContract;
import br.com.rosa.domain.itemContract.RepositoryItemContrato;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import br.com.rosa.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ValidateIfExists  {
	
	@Autowired
	private RepositoryItem repository;

	@Autowired
	private RepositoryCategoria repositoryCategory;
	
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



}
