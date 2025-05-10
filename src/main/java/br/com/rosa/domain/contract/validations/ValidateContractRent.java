package br.com.rosa.domain.contract.validations;

import br.com.rosa.domain.itemContract.ItemContract;

import java.util.Set;

public interface ValidateContractRent {
	
	void validate(Set<ItemContract> dados);

}
