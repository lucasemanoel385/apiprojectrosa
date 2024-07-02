package br.com.rosa.domain.contract.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import br.com.rosa.domain.CreateReadImage;
import br.com.rosa.domain.contract.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.RepositoryContrato;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;
import br.com.rosa.domain.contract.validations.ValidadorContratoAluguel;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.ItemContract;
import br.com.rosa.domain.itemContract.RepositoryItemContrato;
import br.com.rosa.infra.exceptions.ValidacaoException;

@Service
public class ContratoService {
	
	@Autowired
	private RepositoryContrato repository;
	
	@Autowired
	private RepositoryItem repositoryItem;
	
	@Autowired
	private RepositoryItemContrato repositoryItemContrato;

	private final String destinationFolder = "C:\\Users\\lucas\\Documents\\tudo de programação\\ProjetoRosa\\rosa\\src\\main\\java\\assetItem\\";

	@Autowired
	private List<ValidadorContratoAluguel> validar;
	
	public DadosContrato cadastrarContrato(ContratoCadastro dados) {

		var itens = setItensContrato(dados.items(), dados.dateOf(), dados.dateUntil(), SituacaoContrato.ORCAMENTO);
		validar.forEach(v -> v.validar(itens));

		Contract contract = new Contract(dados, LocalDate.now(), itens);
		repository.save(contract);

		List<DataItemsContract> itemsContract = new ArrayList<>();

		contract.getItens().forEach((i) -> {
			itemsContract.add(new DataItemsContract(i.getIdItem(), i.getCod(), i.getName(),
					i.getAmount(), i.getValueItemContract(),
					i.getValueTotalItem(), i.getReplacementValue(), CreateReadImage.takeImage(i.getUrlImg())));
		});

		return new DadosContrato(contract, itemsContract);

	}
	
	public ResponseEntity<Page<ListContract>> listarContrato(Pageable page) {

		var lista = repository.findAll(page).map(ListContract::new);
		System.out.println(lista);
		return ResponseEntity.ok(lista);

	}

	public DadosContrato getContractId(Long id) {

		var contract = repository.getReferenceById(id);

		List<DataItemsContract> itemsContract = new ArrayList<>();

		contract.getItens().forEach((i) -> {
			itemsContract.add(new DataItemsContract(i.getIdItem(), i.getCod(), i.getName(),
					i.getAmount(), i.getValueItemContract(),
					i.getValueTotalItem(), i.getReplacementValue(), CreateReadImage.takeImage(i.getUrlImg())));
		});

		return new DadosContrato(contract, itemsContract);
	}
	
	public void changeContract(UpdateContract data) {

		Contract contract = repository.getReferenceById(data.contractId());
		var itensPrevious = contract.getItens();

		List<ItemContract> itemsCurrent = new ArrayList<ItemContract>();
		if(data.items() != null) {
			for (ItemContract test : itensPrevious) {
				repositoryItemContrato.deleteById(test.getId());
			}
			var itens = setItensContrato(data.items(), data.dateOf(), data.dateUntil(), contract.getContractSituation());
			itemsCurrent.addAll(itens);
		}

		//situacoesContrato(contract);

		validar.forEach(v -> v.validar(itemsCurrent));
		contract.setItens(itemsCurrent);
		contract.atualizarInformacoes(data, itemsCurrent);
		repository.save(contract);


	}
	
	public void changeSituationContract(UpdateSituationContract data) {
		Contract contract = repository.getReferenceById(data.contractId());

		situacoesContrato(contract);

		repository.save(contract);

	}



	private String takeImage(String urlImg) {
		//Pega o caminho da pasta que contem o arquivo de cada item
		Path imgUrl = Paths.get(destinationFolder + urlImg.toLowerCase());
		byte[] img = null;
		String base64Image = null;
		try {
			//Pega o arquivo e tranforma em bytes
			img = Files.readAllBytes(imgUrl);

			//Transformamos o byte em String
			base64Image = Base64.getEncoder().encodeToString(img);

		} catch (IOException e) {

			e.printStackTrace();
		}

		return base64Image;
	}
	
	
	private void situacoesContrato(Contract contract) {
		
		if(contract.getContractSituation() == SituacaoContrato.RESERVADO) {
			throw new ValidacaoException("Contrato já reservado");
		}
		
		switch (contract.getContractSituation()) {
			case ORCAMENTO:
				var itensByContract = contract.getItens();
				validar.forEach(v -> v.validar(itensByContract));
				contract.setContractSituation(SituacaoContrato.RESERVADO);
				contract.setDateContract(LocalDate.now());
				contract.getItens().forEach(item -> item.setContractSituation(SituacaoContrato.RESERVADO));
			break;
			
		case CONCLUIDO: 
			repository.deleteById(contract.getId());
			break;
		}
	}

	
	private List<ItemContract> setItensContrato(List<ContratoItem> dados, LocalDate dateOf, LocalDate dateUntil, SituacaoContrato contractSituation) {
		
		List<ItemContract> itens = new ArrayList<>();
		Item item = null;
		ItemContract itemContrato = null;
        for (ContratoItem t : dados) {
            item = repositoryItem.getReferenceById(t.getId());
            itemContrato = new ItemContract(item, t.getTotal(), dateOf, dateUntil, contractSituation);
            itemContrato.setAmount(t.getAmount());
            //repositoryItemContrato.save(itemContrato);
            itens.add(itemContrato);
        }
		
		return itens;
	}

}
