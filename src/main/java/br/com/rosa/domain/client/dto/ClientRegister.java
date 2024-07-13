package br.com.rosa.domain.client.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientRegister(
		@NotNull(message = "Não pode ser nulo")
		String name,
		@NotNull(message = "Não pode ser nulo")
		LocalDate dateBirth,
		@NotNull(message = "Não pode ser nulo")
		String cpf,
		@NotNull(message = "Não pode ser nulo")
		String rg,
		@NotNull(message = "Não pode ser nulo")
		String cep,
		@NotNull(message = "Não pode ser nulo")
		String city,
		@NotNull(message = "Não pode ser nulo")
		String district,
		@NotNull(message = "Não pode ser nulo")
		String street,
		@NotNull(message = "Não pode ser nulo")
		String number,
		String uf,
		String email,
		String phone1,
		String phone2
		){
}
