package br.com.rosa.domain.client.dto;

import java.time.LocalDate;

import br.com.rosa.domain.address.Address;
import br.com.rosa.domain.client.Client;

public record DataClient(
		Long id,
		String name,
		LocalDate dateBirth,
		String cpfCnpj,
		String email,
		Address endereco,
		String phone1,
		String phone2
	) {
	
	public DataClient(Client client) {
		this(client.getId() ,client.getNameReason(), client.getDateBirthCompanyFormation(), client.getCpfCnpj(), client.getEmail(),
				client.getAddress(), client.getPhone1(), client.getPhone2());
	}

}
