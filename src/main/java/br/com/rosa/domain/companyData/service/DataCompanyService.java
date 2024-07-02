package br.com.rosa.domain.companyData.service;

import br.com.rosa.domain.TransformeAndResizeImage;
import br.com.rosa.domain.companyData.CompanyData;
import br.com.rosa.domain.companyData.RepositoryDataCompany;
import br.com.rosa.domain.companyData.dto.DataCompany;
import br.com.rosa.domain.companyData.dto.GetDataCompany;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DataCompanyService {

	@Autowired
	private RepositoryDataCompany repository;


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
}
