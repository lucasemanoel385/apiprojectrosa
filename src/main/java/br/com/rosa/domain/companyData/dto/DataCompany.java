package br.com.rosa.domain.companyData.dto;

import jakarta.validation.constraints.Null;

public record DataCompany(

        @Null
        String reason,
        @Null
        String fantasyName,
        @Null
        String cnpj,
        @Null
        String street,
        @Null
        String number,
        @Null
        String district,
        @Null
        String cep,
        @Null
        String city,
        @Null
        String uf,
        String phone1,
        String phone2
) {
}
