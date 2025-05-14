package br.com.rosa.domain.contract.validations;

import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import br.com.rosa.domain.itemContract.dto.CheckItemsDTO;
import br.com.rosa.domain.itemContract.dto.ItemsWithContractIdDTO;
import br.com.rosa.infra.exceptions.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CheckItemIfAvaible {

    @Autowired
    private RepositoryItem repositoryItem;

    @Autowired
    private RepositoryItemContract repositoryItemContract;

    public Long validate(ItemsWithContractIdDTO item, CheckItemsDTO checkItemsDTO) {

        Long itemData;

        if (checkItemsDTO.dateStart() == null) {
            itemData = repositoryItemContract.quantityItemsDate(item.getCod(),
                    item.getStartDate(), item.getFinalDate(), SituationContract.RESERVADO);
        } else {
            itemData = repositoryItemContract.quantityItemsDate(item.getCod(),
                    checkItemsDTO.dateStart(), checkItemsDTO.dateFinal(), SituationContract.RESERVADO);
        }

        var itemStock = repositoryItem.getReferenceById(item.getCod());

        if (!(itemStock.getQuantity() >= item.getQuantity())) {

            throw new ValidationException("Item com divergência de estoque. Favor verificar na janela de Produtos. Cod.Item: " + itemStock.getCod());
        }

        var results = itemStock.getQuantity() - itemData;

        return results < 0 ? 0 : results;

    }
}
