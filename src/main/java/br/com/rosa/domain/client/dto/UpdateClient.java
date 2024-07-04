package br.com.rosa.domain.client.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UpdateClient(
		
		@NotNull
		Long id,
		String nameReason,
		String cpfCnpj,
		String rgStateRegistration,
		LocalDate dateBirthCompanyFormation,
		String cep,
		String city,
		String district,
		String street,
		String number,
		String uf,
		String email,
		String phone1,
		String phone2

		) {

}
