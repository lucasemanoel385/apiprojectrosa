package br.com.rosa.controller;

import br.com.rosa.domain.companyData.RepositoryDataCompany;
import br.com.rosa.domain.companyData.dto.DataAccounting;
import br.com.rosa.domain.companyData.dto.DataCompany;
import br.com.rosa.domain.companyData.dto.GetDataCompany;
import br.com.rosa.domain.companyData.service.DataCompanyService;
import br.com.rosa.domain.contract.RepositoryContrato;
import br.com.rosa.domain.expenses.Expenses;
import br.com.rosa.domain.expenses.RepositoryExpenses;
import br.com.rosa.domain.expenses.dto.DataExepenses;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.item.dto.AtualizarItem;
import br.com.rosa.domain.item.dto.DadosItem;
import br.com.rosa.domain.item.dto.ItemCadastro;
import br.com.rosa.domain.item.service.ItemService;
import br.com.rosa.domain.paymentContract.RepositoryPayment;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("data-company")
public class DataCompanyController {
	
	@Autowired
	private RepositoryDataCompany repository;

	@Autowired
	private DataCompanyService service;

	@Autowired
	private RepositoryExpenses expensesRepository;

	@Autowired
	private RepositoryContrato contractRepository;

	@Autowired
	private RepositoryPayment paymentRepository;
	
	@PutMapping
	@Transactional
	public ResponseEntity registerItem(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart(name = "dataCompany") DataCompany data, UriComponentsBuilder uriBuilder) {
		
		service.createOrUpdateData(file, data);

		return ResponseEntity.ok().build();
	}

	@PatchMapping("observation")
	@Transactional
	public ResponseEntity saveObservation(@RequestBody String observation) {

		var company = repository.getReferenceById(1l);

		company.setObservation(observation);

		repository.save(company);

		return ResponseEntity.ok().build();
	}

	@PatchMapping("clauses")
	@Transactional
	public ResponseEntity saveAnnotations(@RequestBody String clauses) {

		var company = repository.getReferenceById(1l);

		company.setClauses(clauses);

		repository.save(company);


		return ResponseEntity.ok().build();
	}
	
	@GetMapping
	public ResponseEntity<GetDataCompany> getDataCompany() {

		var company = service.getDataCompany();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(company);

	}

	@GetMapping("accounting/{month}")
	public ResponseEntity<DataAccounting> getDataAccountig(@PathVariable String month) {

		var y = month.substring(0, 4);
		var m = month.substring(5, 7);
		var yearMonth = y + "-" + m;
		var sumPaymentsMonth = paymentRepository.sumPaymentContainsMonth(yearMonth);
		var sumPaymentsYear = paymentRepository.sumPaymentContainsYear(y);

		var valueExpensesMonth = expensesRepository.sumValueContainsMonth(yearMonth);
		var valueExpensesYear = expensesRepository.sumValueContainsYear(y);


		var listExpenses = expensesRepository.findAllContainsMonth(yearMonth);

		var dtoAnalysisAll = new DataAccounting(sumPaymentsMonth, sumPaymentsYear, valueExpensesMonth, valueExpensesYear, listExpenses);

		return ResponseEntity.ok(dtoAnalysisAll);
	}

	@PostMapping("accounting/expenses")
	@Transactional
	public ResponseEntity registerExpenses(@RequestBody @Valid DataExepenses data) {

		var expenses = new Expenses(data);
		expensesRepository.save(expenses);
		return ResponseEntity.status(201).build();

	}

	@DeleteMapping("accounting/expenses/{id}")
	@Transactional
	public ResponseEntity registerExpenses(@PathVariable Long id) {

		expensesRepository.deleteById(id);
		return ResponseEntity.noContent().build();

	}

	/*@GetMapping("all")
	public ResponseEntity<Page<DadosItem>> getAllItens(@PageableDefault(sort = "name", size = 10000, direction = Direction.DESC) Pageable page) {

		var listItens = service.listItens(page);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(listItens);

	}

	@GetMapping("{id}")
	public ResponseEntity<DadosItem> getItens(@PathVariable Long id) {

		var item = service.getItemId(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(item);

	}
	
	@PatchMapping
	@Transactional
	public ResponseEntity<DadosItem> updateItem(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart(name = "item") AtualizarItem dados) {

		var item = service.updateItem(dados, file);
		
		return ResponseEntity.ok().body(new DadosItem(item, item.getUrlimg()));
		
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity<Page<DadosItem>> deleteItem(@PathVariable Long id) {
		System.out.println(id);
		service.deleteItem(id);
		return ResponseEntity.noContent().build();
		
	}*/

}
