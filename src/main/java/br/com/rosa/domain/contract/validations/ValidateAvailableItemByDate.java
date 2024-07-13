package br.com.rosa.domain.contract.validations;

import br.com.rosa.domain.itemContract.ItemContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import br.com.rosa.infra.exceptions.ValidacaoException;

import java.util.List;
import java.util.Set;

@Component
public class ValidateAvailableItemByDate implements ValidateContractRent {
	
	@Autowired
	private RepositoryItem repositoryItem;
	
	@Autowired
	private RepositoryItemContract repositoryItemContract;
	
	@Override
	public void validate(Set<ItemContract> listItens) {

        for (ItemContract listIten : listItens) {

            // Usar a query do existe nessa de encontrar os itens
            var itemData = repositoryItemContract.quantityItemsDate(listIten.getIdItem(),
                    listIten.getStartDate(), listIten.getFinalDate(), SituationContract.RESERVADO);

            var itemStock = repositoryItem.getReferenceById(listIten.getIdItem());

            if (!(itemStock.getAmount() >= listIten.getAmount())) {

                throw new ValidacaoException("Unidades do item: " + itemStock.getName() + " ultrapassada. Estoque total: " + itemStock.getAmount());
            }

            var itemAmountCurrent = listIten.getAmount();
            var itemReturn = itemStock.getAmount();
            var results = (itemStock.getAmount() - itemAmountCurrent - itemData);

            if (results >= 0) {
                itemStock.setAmount(results);
            } else {
                throw new ValidacaoException("Falta de estoque na data especifica. (" + itemStock + ")");
            }
            itemStock.setAmount(itemReturn);

        }

	}

}
