package br.com.rosa.domain.contract.validations;

import br.com.rosa.domain.itemContract.ItemContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import br.com.rosa.infra.exceptions.ValidationException;

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
            var itemData = repositoryItemContract.quantityItemsDate(listIten.getCod(),
                    listIten.getStartDate(), listIten.getFinalDate(), SituationContract.RESERVADO);

            var itemStock = repositoryItem.getReferenceById(listIten.getCod());

            if (!(itemStock.getQuantity() >= listIten.getQuantity())) {

                throw new ValidationException("Unidades do item: " + itemStock.getName() + " ultrapassada. Estoque total: " + itemStock.getQuantity());
            }

            var itemAmountCurrent = listIten.getQuantity();
            var itemReturn = itemStock.getQuantity();
            var results = (itemStock.getQuantity() - itemAmountCurrent - itemData);

            if (results >= 0) {
                itemStock.setQuantity(results);
            } else {
                throw new ValidationException("Falta de estoque na data especifica. (" + itemStock + ")");
            }
            itemStock.setQuantity(itemReturn);

        }

	}

}
