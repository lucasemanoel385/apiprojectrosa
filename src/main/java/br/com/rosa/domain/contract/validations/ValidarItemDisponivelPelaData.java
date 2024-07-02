package br.com.rosa.domain.contract.validations;

import br.com.rosa.domain.itemContract.ItemContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.RepositoryItemContrato;
import br.com.rosa.infra.exceptions.ValidacaoException;

import java.util.List;

@Component
public class ValidarItemDisponivelPelaData implements ValidadorContratoAluguel {
	
	@Autowired
	private RepositoryItem repositoryItem;
	
	@Autowired
	private RepositoryItemContrato repositoryItemContrato;
	
	@Override
	public void validar(List<ItemContract> listItens) {
		var contract = new Contract();

		for (int i = 0; i < listItens.size(); i++) {

			// Usar a query do existe nessa de encontrar os itens
			var itemData = repositoryItemContrato.quantityItemsDate(listItens.get(i).getIdItem(),
					listItens.get(i).getStartDate().minusDays(2l), listItens.get(i).getFinalDate().plusDays(2l), SituacaoContrato.RESERVADO);
			System.out.println("teste" + listItens.get(i).getIdItem());
			var itemEstoque = repositoryItem.getReferenceById(listItens.get(i).getIdItem());

			if (!(itemEstoque.getAmount() >= listItens.get(i).getAmount())) {
				System.out.println(itemEstoque.getAmount() + listItens.get(i).getAmount());
				throw new ValidacaoException("Unidades do item: " + itemEstoque.getName() + " ultrapassada. Estoque total: " + itemEstoque.getAmount());
			}

			var itemRetorno = itemEstoque.getAmount();
			var resultado = (itemEstoque.getAmount() - itemData);

			if (resultado > 0) {
				itemEstoque.setAmount(resultado);
				contract.getItens().add(listItens.get(i));
			} else {
				throw new ValidacaoException("Falta de estoque na data especifica (" + itemEstoque + ")");
			}
			itemEstoque.setAmount(itemRetorno);

		}

		for (ItemContract itemContract : contract.getItens()) {
			var itensRepetidos = 0;
			for (ItemContract listItem : listItens) {
				if (itemContract.getIdItem().equals(listItem.getIdItem())) {
					itensRepetidos++;
				}
			}
			if (itensRepetidos > 1) {
				throw new ValidacaoException("Itens iguais, favor remover o item duplicado.");
			}
		}

	}

}
