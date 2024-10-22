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

import br.com.rosa.domain.contract.RepositoryContract;
import br.com.rosa.domain.contract.service.ContractService;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("contract")
public class ContractController {
	
	@Autowired
	private RepositoryContract repositoryContract;

	@Autowired
	private RepositoryPayment repositoryPayment;

	@Autowired
	private RepositoryItemContract repositoryItemContrato;
	
	@Autowired
	private ContractService service;
	

	@PostMapping
	@Transactional
	public ResponseEntity<DataContract> registerContract(@RequestBody @Valid ContractRegister dados, UriComponentsBuilder uriBuilder) throws Exception {
		var contrato = service.registerContract(dados);

		var uri = uriBuilder.path("/contract/{id}").buildAndExpand(contrato.id()).toUri();
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

	}

	@GetMapping("{id}")
	public ResponseEntity<DataContract> getContractId(@PathVariable Long id) {

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
	public ResponseEntity<DataContract> updateContract(@RequestBody @Valid UpdateContract data) {

		var contract = service.changeContract(data);

		return ResponseEntity.ok(contract);

	}

	@GetMapping("payment/{id}")
	public ResponseEntity<List<Payment>> getPayments(@PathVariable Long id) {

		var contract = repositoryContract.getReferenceById(id);

		return ResponseEntity.ok(contract.getPayment());

	}

	@GetMapping("cod/{cod}")
	public ResponseEntity<List<ListContract>> getItemsReservedInContract(@PathVariable Long cod) {

			return ResponseEntity.ok(service.getItemsReservedInContract(cod));
	}

	@PatchMapping("payment/{id}")
	@Transactional
	public ResponseEntity<List<Payment>> payment(@PathVariable Long id,@Valid @RequestBody RegisterPayment pay) {

		var contract = repositoryContract.getReferenceById(id);

		for (Payment p : contract.getPayment()) {
			repositoryPayment.deleteById(p.getId());
		}

		List<Payment> listPayment = new ArrayList<>();
        for (RegisterDataPayment payment : pay.payments()) {
            var p = new Payment(payment.paymentValue(), payment.datePayment(), contract);
            listPayment.add(p);
        }

		contract.setPayment(listPayment);

		repositoryContract.save(contract);

		return ResponseEntity.ok(listPayment);

	}

	@PatchMapping("situation")
	@Transactional
	public ResponseEntity updateSituationContrato(@RequestBody @Valid UpdateSituationContract data) {
		service.changeSituationContract(data);
		return ResponseEntity.ok().build();
	}
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity deleteContract(@PathVariable Long id) {

		service.deleteContractById(id);

		return ResponseEntity.noContent().build();

	}
	

}
