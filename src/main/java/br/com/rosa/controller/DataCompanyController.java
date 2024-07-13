package br.com.rosa.controller;

import br.com.rosa.domain.companyData.RepositoryDataCompany;
import br.com.rosa.domain.companyData.dto.DataAccounting;
import br.com.rosa.domain.companyData.dto.DataCompany;
import br.com.rosa.domain.companyData.dto.DtoCommissionEmployee;
import br.com.rosa.domain.companyData.dto.GetDataCompany;
import br.com.rosa.domain.companyData.service.DataCompanyService;
import br.com.rosa.domain.contract.RepositoryContract;
import br.com.rosa.domain.expenses.Expenses;
import br.com.rosa.domain.expenses.RepositoryExpenses;
import br.com.rosa.domain.expenses.dto.DataExepenses;
import br.com.rosa.domain.paymentContract.RepositoryPayment;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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
	private RepositoryContract contractRepository;

	@Autowired
	private RepositoryPayment paymentRepository;
	
	@PutMapping
	@Transactional
	public ResponseEntity registerItem(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart(name = "dataCompany") DataCompany data, UriComponentsBuilder uriBuilder) throws IOException {
		
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

		var dtoAnalysisAll = service.dataAccouting(month);
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
