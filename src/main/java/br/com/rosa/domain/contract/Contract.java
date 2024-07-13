package br.com.rosa.domain.contract;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.paymentContract.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rosa.domain.contract.dto.UpdateContract;
import br.com.rosa.domain.contract.dto.ContractRegister;
import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.contract.employee.Employee;
import br.com.rosa.domain.itemContract.ItemContract;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Contract {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "client_id")
	private Client client;
	private LocalDate dateContract;
	private LocalDate startDate;
	private LocalDate finalDate;
	@OneToMany(cascade = CascadeType.ALL)
	private Set<ItemContract> itens = new HashSet<>();
	private double value;
	private double discount;
	private double valueTotal;
	@Enumerated(EnumType.STRING)
	private SituationContract contractSituation;
	@Embedded
	private Employee seller;
	private String observation;
	private String annotations;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contract", fetch = FetchType.LAZY)
	private List<Payment> payment = new ArrayList<>();
	
	public Contract(ContractRegister data, LocalDate dateNow, Set<ItemContract> itens) {

		this.client = new Client(data.client());
		this.dateContract = dateNow;
		this.startDate = data.dateOf();
		this.finalDate = data.dateUntil();
		this.itens = itens;
		this.value = this.valueAllItems(itens);
		this.discount = data.discount();
		this.valueTotal = this.valorTotal(data.discount(), this.valueAllItems(itens));
		this.contractSituation = SituationContract.ORCAMENTO;
		this.seller = new Employee(data.seller());
		this.observation = data.observation();
		this.annotations = data.annotations();
	}

	public void setClient(Long cliente) {
		this.client = new Client(cliente);
	}

	public Contract(Long id2) {
		this.id = id2;
	}
	
	private double valueAllItems(Set<ItemContract> itens) {
		double valor = (float) 0;
		for (ItemContract itemContrato : itens) {
			valor += itemContrato.getValueItemContract() * itemContrato.getAmount();
		}
		return valor;
	}
	
	public void updateAtrb(@Valid UpdateContract data, Set<ItemContract> itens) {
		if (data.clientId() != null && !(this.getClient().getId().equals(data.clientId()))) {
			this.setClient(data.clientId());
		}
		if(data.dateOf() != null && !(this.startDate.equals(data.dateOf()))) {
			this.startDate = data.dateOf();
		}
		if(data.dateUntil() != null && !(this.finalDate.equals(data.dateUntil()))) {
			this.finalDate = data.dateUntil();
		}
		if(data.discount() >= 0 && !(this.discount == data.discount())) {
			this.discount = data.discount();
		}
		if(data.items() != null) {
			this.value = this.valueAllItems(itens);
			this.valueTotal =  this.valorTotal(this.discount, this.valueAllItems(itens));
		}
		if(data.observation() != null && !(this.observation.equals(data.observation()))) {
			this.observation = data.observation();
		}
		if(data.annotations() != null && !(this.annotations.equals(data.annotations()))) {
			this.annotations = data.annotations();
		}
		if(data.seller() != null && !(this.seller.getSeller().equals(data.seller()))) {
			this.seller.setSeller(data.seller());
		}
	}
	
	private double valorTotal(double desconto, double valor) {
		return (desconto == -1 || desconto == 0) ?
				this.value : this.value - ((valor * desconto) / 100);
		
	}
}


