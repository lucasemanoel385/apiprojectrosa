package br.com.rosa.domain.companyData.dto;

import br.com.rosa.domain.companyData.CompanyData;

public record GetDataCompany(
    String reason,
    String fantasyName,
    String cnpj,
    String street,
    String number,
    String district,
    String cep,
    String city,
    String uf,
    String phone1,
    String phone2,
    String imagem,
    String observation,
    String clauses) {

    public GetDataCompany(CompanyData company, String imagem) {
        this(company.getReason(), company.getFantasyName(), company.getCnpj(),
                company.getStreet(), company.getNumber(), company.getDistrict(),
                company.getCep(), company.getCity(), company.getUf(),
                company.getPhone1(), company.getPhone2(), imagem,
                company.getObservation(), company.getClauses());
    }
}
