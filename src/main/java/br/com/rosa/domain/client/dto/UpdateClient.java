package br.com.rosa.domain.client.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateClient(
		
		@NotNull
		Long id,
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
