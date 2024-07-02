package br.com.rosa.controller;

import br.com.rosa.domain.contract.RepositoryContrato;
import br.com.rosa.domain.companyData.dto.DataAccounting;
import br.com.rosa.domain.expenses.Expenses;
import br.com.rosa.domain.expenses.RepositoryExpenses;
import br.com.rosa.domain.expenses.dto.DataExepenses;
import br.com.rosa.domain.paymentContract.RepositoryPayment;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("accounting")
public class AnalysisController {

	@Autowired
	private RepositoryExpenses expensesRepository;

	@Autowired
	private RepositoryContrato contractRepository;

	@Autowired
	private RepositoryPayment paymentRepository;

	@GetMapping("{month}")
	public ResponseEntity<DataAccounting> getDataAccountig(@PathVariable String month) {

		var y = month.substring(0, 4);
		var m = month.substring(6, 7);

		var sumPaymentsMonth = paymentRepository.sumPaymentContainsMonth(m);
		var sumPaymentsYear = paymentRepository.sumPaymentContainsYear(y);

		var valueExpensesMonth = expensesRepository.sumValueContainsMonth(m);
		var valueExpensesYear = expensesRepository.sumValueContainsYear(y);


		var listExpenses = expensesRepository.findAllContainsMonth(m);

		var dtoAnalysisAll = new DataAccounting(sumPaymentsMonth, sumPaymentsYear, valueExpensesMonth, valueExpensesYear, listExpenses);

		return ResponseEntity.ok(dtoAnalysisAll);
	}

	@PostMapping("/expenses")
	@Transactional
	public ResponseEntity registerExpenses(@RequestBody @Valid DataExepenses data) {

		var expenses = new Expenses(data);
		expensesRepository.save(expenses);
		return ResponseEntity.status(201).build();

	}

	@DeleteMapping("/expenses/{id}")
	@Transactional
	public ResponseEntity registerExpenses(@PathVariable Long id) {

		expensesRepository.deleteById(id);
		return ResponseEntity.noContent().build();

	}

}
