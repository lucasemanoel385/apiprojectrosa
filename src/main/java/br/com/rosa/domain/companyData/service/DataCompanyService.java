package br.com.rosa.domain.companyData.service;

import br.com.rosa.domain.TransformeAndResizeImage;
import br.com.rosa.domain.companyData.CompanyData;
import br.com.rosa.domain.companyData.RepositoryDataCompany;
import br.com.rosa.domain.companyData.dto.DataAccounting;
import br.com.rosa.domain.companyData.dto.DataCompany;
import br.com.rosa.domain.companyData.dto.DtoCommissionEmployee;
import br.com.rosa.domain.companyData.dto.GetDataCompany;
import br.com.rosa.domain.contract.RepositoryContract;
import br.com.rosa.domain.expenses.RepositoryExpenses;
import br.com.rosa.domain.paymentContract.RepositoryPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DataCompanyService {

	@Autowired
	private RepositoryDataCompany repository;

	@Autowired
	private RepositoryPayment paymentRepository;

	@Autowired
	private RepositoryExpenses expensesRepository;

	@Autowired
	private RepositoryContract contractRepository;


	public CompanyData createOrUpdateData(MultipartFile file, DataCompany data) throws IOException {

		if (!(repository.existsById(1l))) {

			 var company = new CompanyData(data, file.getBytes());

			repository.save(company);

			return company;

		} else {
			var company = repository.getReferenceById(1l);

			company.setReason(data.reason());
			company.setFantasyName(data.fantasyName());
			company.setCnpj(data.cnpj());
			company.setStreet(data.street());
			company.setNumber(data.number());
			company.setDistrict(data.district());
			company.setCep(data.cep());
			company.setCity(data.city());
			company.setUf(data.uf());
			company.setPhone1(data.phone1());
			company.setPhone2(data.phone2());

			if (!(file == null)) {
				company.setImg(file.getBytes());
			}
			repository.save(company);
			return company;
		}
	}

	public GetDataCompany getDataCompany() {

		var company = repository.getReferenceById(1l);

		var base64imagem = TransformeAndResizeImage.takeImage(company.getImg());

		return new GetDataCompany(company, base64imagem);

	}

    public DataAccounting dataAccouting(String month) {

		var y = month.substring(0, 4);
		var m = month.substring(5, 7);
		var yearMonth = y + "-" + m;
		var sumPaymentsMonth = paymentRepository.sumPaymentContainsMonth(yearMonth);
		var sumPaymentsYear = paymentRepository.sumPaymentContainsYear(y);

		var valueExpensesMonth = expensesRepository.sumValueContainsMonth(yearMonth);
		var valueExpensesYear = expensesRepository.sumValueContainsYear(y);


		var listExpenses = expensesRepository.findAllContainsMonth(yearMonth);

		List<Object[]> commissionObject = contractRepository.findAllSellerAndValuePaymentOfMonth(yearMonth);
		var commission =  commissionObject.stream()
				.map(result -> new DtoCommissionEmployee((String) result[0], ((BigDecimal) result[1])))
				.collect(Collectors.toList());

		return new DataAccounting(sumPaymentsMonth, sumPaymentsYear, valueExpensesMonth, valueExpensesYear, listExpenses, commission);

    }
}
