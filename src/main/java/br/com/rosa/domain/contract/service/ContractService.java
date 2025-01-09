package br.com.rosa.domain.contract.service;

import java.time.LocalDate;
import java.util.*;

import br.com.rosa.domain.TransformAndResizeImage;
import br.com.rosa.domain.contract.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.RepositoryContract;
import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.contract.validations.ValidateContractRent;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.ItemContract;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import br.com.rosa.infra.exceptions.ValidationException;

@Service
public class ContractService {
	
	@Autowired
	private RepositoryContract repository;
	
	@Autowired
	private RepositoryItem repositoryItem;
	
	@Autowired
	private RepositoryItemContract repositoryItemContrato;

	@Autowired
	private List<ValidateContractRent> validate;

	public DataContract registerContract(ContractRegister data) {

		var items = setItemsContract(data.items(), data.dateOf(), data.dateUntil(), SituationContract.ORCAMENTO);
		Set<ItemContract> itemsContracts = new HashSet<>(items.values());
		validate.forEach(v -> v.validate(itemsContracts));

		Contract contract = new Contract(data, LocalDate.now(), itemsContracts);
		repository.save(contract);

		var itemsContract = getImgOfItens(contract);

		return new DataContract(contract, itemsContract);

	}

	public DataContract getContractId(Long id) {

		var contract = repository.getReferenceById(id);

		var itemsContract = getImgOfItens(contract);

		return new DataContract(contract, itemsContract);
	}
	
	public DataContract changeContract(UpdateContract data) {

		Contract contract = repository.getReferenceById(data.contractId());
		var itensPrevious = contract.getItens();

		Map<Long, ItemContract> itemsCurrent = new HashMap<>();

		if(data.items() != null) {
			for (ItemContract test : itensPrevious) {
				repositoryItemContrato.deleteById(test.getId());
			}
			var itens = setItemsContract(data.items(), data.dateOf(), data.dateUntil(), contract.getContractSituation());
			itemsCurrent.putAll(itens);
		}

		if (contract.getContractSituation() == SituationContract.RESERVADO) {
			for (ItemContract x : itensPrevious) {
				if (itemsCurrent.containsKey(x.getCod())) {
					itemsCurrent.get(x.getCod()).setValueItemContract(x.getValueItemContract());
				}
			}
		}

		Set<ItemContract> itemsUpdate = new HashSet<>(itemsCurrent.values());

		validate.forEach(v -> v.validate(itemsUpdate));
		contract.setItens(itemsUpdate);
		contract.updateAtrb(data, itemsUpdate);
		repository.save(contract);

		var itemsContract = getImgOfItens(contract);

		return new DataContract(contract, itemsContract);


	}
	
	public void changeSituationContract(UpdateSituationContract data) {
		Contract contract = repository.getReferenceById(data.contractId());

		updateSituationContract(contract);

		repository.save(contract);

	}

	private List<DataItemsContract> getImgOfItens(Contract contract) {

		List<DataItemsContract> itemsContract = new ArrayList<>();

		contract.getItens().forEach((i) -> {
			var img = repositoryItem.existsById(i.getCod()) ? repositoryItem.getReferenceById(i.getCod()).getImg() : null;
			itemsContract.add(new DataItemsContract(i.getId(), i.getCod(), i.getName(),
					i.getQuantity(), i.getValueItemContract(),
					i.getValueTotalItem(), i.getReplacementValue(), TransformAndResizeImage.takeImage(img)));
		});

		return itemsContract;

	}

	private void updateSituationContract(Contract contract) {
		
		/*if(contract.getContractSituation() == SituationContract.RESERVADO) {
			throw new ValidationException("Contrato já reservado");
		}*/
		switch (contract.getContractSituation()) {
            case ORCAMENTO:
				var itensByContract = contract.getItens();
				validate.forEach(v -> v.validate(itensByContract));
				contract.setContractSituation(SituationContract.RESERVADO);
				contract.setDateContract(LocalDate.now());
				contract.getItens().forEach(item -> item.setContractSituation(SituationContract.RESERVADO));
			break;
			case RESERVADO:
				throw new ValidationException("Contrato já reservado");
            case CONCLUIDO:
				repository.deleteById(contract.getId());
			break;
		}
	}

	
	private Map<Long, ItemContract> setItemsContract(List<ContractItem> dataItems, LocalDate dateOf, LocalDate dateUntil, SituationContract contractSituation) {
		
		Map<Long, ItemContract> items = new HashMap<>();

        for (ContractItem t : dataItems) {
			Item item = null;
			ItemContract itemContrato = null;
            item = repositoryItem.getReferenceByCod(t.getId());
            itemContrato = new ItemContract(item, t.getValueItem(), dateOf, dateUntil, contractSituation);
            itemContrato.setQuantity(t.getAmount());
			if (items.containsKey(itemContrato.getCod())) {
				throw new ValidationException("Itens iguais, favor remover o item duplicado.");
			}
            items.put(itemContrato.getCod(), itemContrato);
        }
		
		return items;
	}

    public void deleteContractById(Long id) {
		var dateNow = LocalDate.now().minusMonths(6).toString();
		var yearNow =  LocalDate.now().getYear();
		var deleteContractsReservedOnDate = yearNow + "-" + "01-01";

		repository.deleteContractsBudgetsWithinSixMonthsAgo(dateNow);
		repository.deleteContractsReservationsWithinOneYearAgo(deleteContractsReservedOnDate);
		repository.deleteById(id);

	}

    public List<ListContract> getItemsReservedInContract(Long cod) {

		var t = repository.getItemsContractId(cod);
		List<Contract> listContract = new ArrayList<>();
		t.forEach(i -> listContract.add(repository.getReferenceById(i)));
		return listContract.stream().map(ListContract::new).toList();

    }
}
