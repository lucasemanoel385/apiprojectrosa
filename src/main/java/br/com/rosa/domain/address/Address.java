package br.com.rosa.domain.address;

import br.com.rosa.domain.address.dto.DataAddress;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
	private String cep;
	private String city;
	private String district;
	private String street;
	private String number;
	private String uf;
	
	public Address(DataAddress data) {
		this.cep = data.cep();
		this.street = data.street();
		this.district = data.district();
		this.city = data.city();
		this.number = data.number();
		this.uf = data.uf();
	}
	
}
