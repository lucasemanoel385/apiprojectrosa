package br.com.rosa.domain.address.dto;

public record DataAddress(
		String cep,
		String street,
		String number,
		String district,
		String city,
		String uf
		) {

}
