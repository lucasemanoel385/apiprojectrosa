package br.com.rosa.domain.contract.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		validate.forEach(v -> v.validate(items));

		Contract contract = new Contract(data, LocalDate.now(), items);
		repository.save(contract);

		var itemsContract = getImgOfItens(contract);

		return new DataContract(contract, itemsContract);

	}
	
	/*public ResponseEntity<Page<ListContract>> listarContrato(Pageable page) {

		var lista = repository.findAll(page).map(ListContract::new);
		System.out.println(lista);
		return ResponseEntity.ok(lista);

	}*/

	public DataContract getContractId(Long id) {

		var contract = repository.getReferenceById(id);

		var itemsContract = getImgOfItens(contract);

		return new DataContract(contract, itemsContract);
	}
	
	public DataContract changeContract(UpdateContract data) {

		Contract contract = repository.getReferenceById(data.contractId());
		var itensPrevious = contract.getItens();

		Set<ItemContract> itemsCurrent = new HashSet<>();

		if(data.items() != null) {
			for (ItemContract test : itensPrevious) {
				repositoryItemContrato.deleteById(test.getId());
			}
			var itens = setItemsContract(data.items(), data.dateOf(), data.dateUntil(), contract.getContractSituation());
			itemsCurrent.addAll(itens);
		}

		if (contract.getContractSituation() == SituationContract.RESERVADO) {
			for (ItemContract x : itemsCurrent) {
				for (ItemContract y : itensPrevious) {
					if (x.getIdItem().equals(y.getIdItem())) {
						x.setValueItemContract(y.getValueItemContract());
					}
				}
			}
		}

		validate.forEach(v -> v.validate(itemsCurrent));
		contract.setItens(itemsCurrent);
		contract.updateAtrb(data, itemsCurrent);
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
			var img = repositoryItem.existsById(i.getIdItem()) ? repositoryItem.getReferenceById(i.getIdItem()).getImg() : null;
			itemsContract.add(new DataItemsContract(i.getIdItem(), i.getCod(), i.getName(),
					i.getAmount(), i.getValueItemContract(),
					i.getValueTotalItem(), i.getReplacementValue(), TransformAndResizeImage.takeImage(img)));
		});

		return itemsContract;

	}

	private void updateSituationContract(Contract contract) {
		
		if(contract.getContractSituation() == SituationContract.RESERVADO) {
			throw new ValidationException("Contrato já reservado");
		}
		
		switch (contract.getContractSituation()) {
			case ORCAMENTO:
				var itensByContract = contract.getItens();
				validate.forEach(v -> v.validate(itensByContract));
				contract.setContractSituation(SituationContract.RESERVADO);
				contract.setDateContract(LocalDate.now());
				contract.getItens().forEach(item -> item.setContractSituation(SituationContract.RESERVADO));
			break;
			
		case CONCLUIDO: 
			repository.deleteById(contract.getId());
			break;
		}
	}

	
	private Set<ItemContract> setItemsContract(List<ContractItem> dataItems, LocalDate dateOf, LocalDate dateUntil, SituationContract contractSituation) {
		
		Set<ItemContract> items = new HashSet<>();


        for (ContractItem t : dataItems) {
			Item item = null;
			ItemContract itemContrato = null;
            item = repositoryItem.getReferenceById(t.getId());
            itemContrato = new ItemContract(item, dateOf, dateUntil, contractSituation);
            itemContrato.setAmount(t.getAmount());
			if (!items.add(itemContrato)) {
				throw new ValidationException("Itens iguais, favor remover o item duplicado.");
			}
            items.add(itemContrato);
        }
		
		return items;
	}

    public void deleteContractById(Long id) {
		var dateNow = LocalDate.now().minusMonths(6).toString();
		var yearNow =  LocalDate.now().getYear();
		var deleteContractsReservedOnDate = yearNow + "-" + "01-28";

		repository.deleteContractsBudgetsFromSixMonthsAgo(dateNow);
		repository.deleteContractsReservationsFromOneYearAgo(deleteContractsReservedOnDate);
		repository.deleteById(id);

	}
}
