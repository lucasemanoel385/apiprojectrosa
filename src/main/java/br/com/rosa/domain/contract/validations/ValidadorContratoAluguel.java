package br.com.rosa.domain.contract.validations;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.itemContract.ItemContract;

import java.util.List;

public interface ValidadorContratoAluguel {
	
	void validar(List<ItemContract> dados);

}
