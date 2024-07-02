package br.com.rosa.domain.companyData.service;

import br.com.rosa.domain.CreateReadImage;
import br.com.rosa.domain.companyData.CompanyData;
import br.com.rosa.domain.companyData.RepositoryDataCompany;
import br.com.rosa.domain.companyData.dto.DataCompany;
import br.com.rosa.domain.companyData.dto.GetDataCompany;
import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.RepositoryContrato;
import br.com.rosa.domain.contract.dto.*;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;
import br.com.rosa.domain.contract.validations.ValidadorContratoAluguel;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.item.dto.ItemCadastro;
import br.com.rosa.domain.itemContract.ItemContract;
import br.com.rosa.domain.itemContract.RepositoryItemContrato;
import br.com.rosa.infra.exceptions.ValidacaoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class DataCompanyService {

	@Autowired
	private RepositoryDataCompany repository;


	public CompanyData createOrUpdateData(MultipartFile file, DataCompany data) {

		if (!(repository.existsById(1l))) {

			var urlImg = CreateReadImage.saveImgItem(file);

			 var company = new CompanyData(data, urlImg);

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
				CreateReadImage.deleteImg(company.getUrlImg());
				company.setUrlImg(CreateReadImage.saveImgItem(file));
			}
			repository.save(company);
			return company;
		}
	}

	public GetDataCompany getDataCompany() {

		var company = repository.getReferenceById(1l);

		var base64imagem = CreateReadImage.takeImage(company.getUrlImg());

		return new GetDataCompany(company, base64imagem);

	}
}
