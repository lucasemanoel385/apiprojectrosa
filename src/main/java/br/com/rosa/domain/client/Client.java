package br.com.rosa.domain.client;

import br.com.rosa.domain.address.dto.DataAddress;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rosa.domain.client.dto.UpdateClient;
import br.com.rosa.domain.client.dto.ClientRegister;
import br.com.rosa.domain.person.People;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Client extends People {

	private String nameReason;
	private String cpfCnpj;
	private String rgStateRegistration;
	private LocalDate dateBirthCompanyFormation;
	
	public Client(ClientRegister data, DataAddress addrees) {

		super(data.email(), addrees, data.phone1(), data.phone2());
		this.nameReason = data.name();
		this.cpfCnpj = data.cpf();
		this.rgStateRegistration = data.rg();
		this.dateBirthCompanyFormation = data.dateBirth();
	}
	
	public Client(Long id) {
		
		super(id);
	}


	public Client(long l, ClientRegister data, DataAddress address) {

		super(l,data.email(), address, data.phone1(), data.phone2());
		this.nameReason = data.name();
		this.cpfCnpj = data.cpf();
		this.rgStateRegistration = data.rg();
		this.dateBirthCompanyFormation = data.dateBirth();

	}

	public void updateTheInformation(UpdateClient data) {
		if(data.nameReason() != null && !this.getNameReason().equals(data.nameReason())) {
			this.setNameReason(data.nameReason());
		}
		if(data.cpfCnpj() != null && !this.getCpfCnpj().equals(data.cpfCnpj())) {
			this.setCpfCnpj(data.cpfCnpj());
		}

		this.setRgStateRegistration(data.rgStateRegistration());

		this.setDateBirthCompanyFormation(data.dateBirthCompanyFormation());

		if(data.email() != null && !this.getEmail().equals(data.email())) {
			super.setEmail(data.email());
		}
		if(data.cep() != null && !this.getAddress().getCep().equals(data.cep())) {
			super.getAddress().setCep(data.cep());
		}
		if(data.street() != null && !this.getAddress().getStreet().equals(data.street())) {
			super.getAddress().setStreet(data.street());
		}
		if(data.city() != null && !this.getAddress().getCity().equals(data.city())) {
			super.getAddress().setCity(data.city());
		}
		if(data.district() != null && !this.getAddress().getDistrict().equals(data.district())) {
			super.getAddress().setDistrict(data.district());
		}
		if(data.number() != null && !this.getAddress().getNumber().equals(data.number())) {
			super.getAddress().setNumber(data.number());
		}
		if(data.phone1() != null) {
			super.setPhone1(data.phone1());
		}

		if(data.phone2() != null) {
			super.setPhone2(data.phone2());
		}
		if(data.complement() != null) {
			super.getAddress().setComplement(data.complement());
		}
		
	}
	
}
