package br.com.rosa.domain.client.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ClientRegister(
		@NotNull(message = "Não pode ser nulo")
		String name,
		LocalDate dateBirth,
		@NotNull(message = "Não pode ser nulo")
		String cpf,
		String rg,
		@NotNull(message = "Não pode ser nulo")
		String cep,
		@NotNull(message = "Não pode ser nulo")
		String city,
		@NotNull(message = "Não pode ser nulo")
		String district,
		String complement,
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
