package br.com.rosa.domain.client.dto;

import java.time.LocalDate;

public record ClientRegister(
		String name,
		LocalDate dateBirth,
		String cpf,
		String rg,
		String cep,
		String city,
		String district,
		String street,
		String number,
		String uf,
		String email,
		String phone1,
		String phone2
		){
}
