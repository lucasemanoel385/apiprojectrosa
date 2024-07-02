package br.com.rosa.domain.person;

import br.com.rosa.domain.address.Address;
import br.com.rosa.domain.address.dto.DataAddress;
import jakarta.persistence.Embedded;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public abstract class Pessoa {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String email;
	@Embedded
	private Address address;
	private String phone1;
	private String phone2;
	
	public Pessoa(String email, DataAddress addrees, String phone1, String phone2) {
		this.email = email;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.address = new Address(addrees);

	}

	public Pessoa(Long id) {
		this.id = id;
	}
}
