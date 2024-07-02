package br.com.rosa.controller;

import br.com.rosa.domain.contract.dto.*;
import br.com.rosa.domain.paymentContract.Payment;
import br.com.rosa.domain.paymentContract.RepositoryPayment;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rosa.domain.contract.RepositoryContrato;
import br.com.rosa.domain.contract.service.ContratoService;
import br.com.rosa.domain.itemContract.RepositoryItemContrato;
import jakarta.transaction.Transactional;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RestController
@RequestMapping("contrato")
public class ContratoController {
	
	@Autowired
	private RepositoryContrato repositoryContract;

	@Autowired
	private RepositoryPayment repositoryPayment;

	@Autowired
	private RepositoryItemContrato repositoryItemContrato;
	
	@Autowired
	private ContratoService service;
	

	@PostMapping
	@Transactional
	public ResponseEntity<DadosContrato> registerContract(@RequestBody @Valid ContratoCadastro dados, UriComponentsBuilder uriBuilder) throws Exception {
		var contrato = service.cadastrarContrato(dados);

		var uri = uriBuilder.path("/contrato/{id}").buildAndExpand(contrato.id()).toUri();
		return ResponseEntity.created(uri).body(contrato);
	}

	@GetMapping
	public ResponseEntity<Page<ListContract>> getAllContract(
			@PageableDefault(page = 0, size = 5, sort = "start_date" ,direction = Sort.Direction.ASC) Pageable page,
			@RequestParam(required = false) String search) {


		return (search == null) ?
					ResponseEntity.ok(repositoryContract.findAll(page).map(ListContract::new))
						:
							ResponseEntity.ok(repositoryContract.findAllByIdOrByClientNameOrByClientCpf(search, page).map(ListContract::new));
		/*if (search == null ) {
			return ResponseEntity.ok(repositoryContract.findAll(page).map(ListContract::new));
		} else {
			return ResponseEntity.ok(repositoryContract.findAllByIdOrByClientNameOrByClientCpf(search, page).map(ListContract::new));
		}
		//return service.listarContrato(page);*/

	}

	@GetMapping("{id}")
	public ResponseEntity<DadosContrato> getContractId(@PathVariable Long id) {

		var dataContract = service.getContractId(id);

		return ResponseEntity.ok(dataContract);

	}

	@GetMapping("contractMonth/{month}")
	public ResponseEntity<List<ListContract>> getListContractMonthReserve(@PathVariable String month) {

		var listContractMonth = repositoryContract.findAllByStartDate(month).stream().map(ListContract::new).toList();

		return ResponseEntity.ok(listContractMonth);

	}

	@GetMapping("budgets/{month}")
	public ResponseEntity<ContentDashBoard> getDataBudgetMonth(@PathVariable String month) {

		var countContractMonth =
				repositoryContract.dataContractByMonth(month);

		var countContractMonthReserve =
				repositoryContract.dataContractByMonthReserve(month);

		var contentDashBoard = new ContentDashBoard(countContractMonth, countContractMonthReserve);

		return ResponseEntity.ok(contentDashBoard);

	}

	@PatchMapping
	@Transactional
	public ResponseEntity updateContract(@RequestBody @Valid UpdateContract data) {

		service.changeContract(data);

		return ResponseEntity.ok().build();

	}

	@GetMapping("payment/{id}")
	public ResponseEntity<List<Payment>> getPayments(@PathVariable Long id) {

		var contract = repositoryContract.getReferenceById(id);

		return ResponseEntity.ok(contract.getPayment());

	}

	@PatchMapping("payment/{id}")
	@Transactional
	public ResponseEntity<List<Payment>> payment(@PathVariable Long id,@Valid @RequestBody RegisterPayment pay) {
		System.out.println(pay);
		var contract = repositoryContract.getReferenceById(id);

		for (Payment p : contract.getPayment()) {
			repositoryPayment.deleteById(p.getId());
		}

		List<Payment> listPayment = new ArrayList<>();
        for (RegisterDataPayment payment : pay.payments()) {
            var p = new Payment(payment.paymentValue(), payment.datePayment());
            listPayment.add(p);
        }

		contract.setPayment(listPayment);

		repositoryContract.save(contract);

		return ResponseEntity.ok(listPayment);

	}

	@DeleteMapping("itemContrato/{id}")
	@Transactional
	public ResponseEntity deleteItemContract(@PathVariable Long id) {
	//	var itemExcluido = repositoryItemContrato.getReferenceById(id);
	//	List<Item> itens = new ArrayList<>();
	//	contrato.setItens(itens);
		repositoryItemContrato.deleteById(id);
		return ResponseEntity.noContent().build();

	}

	@PatchMapping("situation")
	@Transactional
	public ResponseEntity updateSituationContrato(@RequestBody @Valid UpdateSituationContract data) {
		System.out.println(data);
		service.changeSituationContract(data);

		return ResponseEntity.ok().build();

//		Contrato contrato = repository.getReferenceById(dados.contratoId());
//
//		if (SituacaoContrato.RESERVADO == dados.situacaoContrato()) {
//			for (int i = 0; i < contrato.getItens().size(); i++) {
//				//Usar a query do existe nessa de encontrar os itens
//				var itemData = repositoryItemContrato.quantityItemsDate(contrato.getItens().get(i).getIdItem(),
//						contrato.getDataInicio(), contrato.getDataFinal());
//				var itemEstoque = repositoryItem.getReferenceById(contrato.getItens().get(i).getIdItem());
//				var itemRetorno = itemEstoque.getQuantidade();
//				var resultado = (itemEstoque.getQuantidade() - itemData) +1 ;
//				if(resultado > 0) {
//					itemEstoque.setQuantidade(resultado);
//					contrato.setSituacaoContrato(dados.situacaoContrato());
//				} else {
//					throw new Exception("falta de estoque na data especifica" + itemEstoque);
//				}
//				itemEstoque.setQuantidade(itemRetorno);
//			}
//		}
//
//		repository.save(contrato);

	}

	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity deleteContract(@PathVariable Long id) {
		repositoryContract.deleteById(id);
		return ResponseEntity.noContent().build();

	}
	

}
