package br.com.rosa.domain.contract;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.paymentContract.Payment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rosa.domain.contract.dto.UpdateContract;
import br.com.rosa.domain.contract.dto.ContratoCadastro;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;
import br.com.rosa.domain.contract.funcionario.Funcionario;
import br.com.rosa.domain.itemContract.ItemContract;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	private Client client;
	private LocalDate dateContract;
	private LocalDate startDate;
	private LocalDate finalDate;
	@OneToMany(cascade = CascadeType.ALL)
	private List<ItemContract> itens = new ArrayList<>();
	private double value;
	private double discount;
	private double valueTotal;
	@Enumerated(EnumType.STRING)
	private SituacaoContrato contractSituation;
	@Embedded
	private Funcionario funcionario;
	private String observation;
	private String annotations;
	@OneToMany(cascade = CascadeType.ALL)
	private List<Payment> payment;
	
	public Contract(ContratoCadastro data, LocalDate dateNow, List<ItemContract> itens) {

		this.client = new Client(data.client());
		this.dateContract = dateNow;
		this.startDate = data.dateOf();
		this.finalDate = data.dateUntil();
		this.itens = itens;
		this.value = this.valorItens(itens);
		this.discount = data.discount();
		this.valueTotal = this.valorTotal(data.discount(), this.valorItens(itens));
		this.contractSituation = SituacaoContrato.ORCAMENTO;
		this.funcionario = new Funcionario(data.seller());
		this.observation = data.observation();
		this.annotations = data.annotations();
	}

	public void setClient(Long cliente) {
		this.client = new Client(cliente);
	}

	public Contract(Long id2) {
		this.id = id2;
	}
	
	private double valorItens(List<ItemContract> itens) {
		double valor = (float) 0;
		for (ItemContract itemContrato : itens) {
			valor += itemContrato.getValueItemContract() * itemContrato.getAmount();
		}
		return valor;
	}
	
	public void atualizarInformacoes(@Valid UpdateContract dados, List<ItemContract> itens) {
		if (dados.clientId() != null && !(this.getClient().getId().equals(dados.clientId()))) {
			this.setClient(dados.clientId());
		}
		if(dados.dateOf() != null && !(this.startDate.equals(dados.dateOf()))) {
			this.startDate = dados.dateOf();
		}
		if(dados.dateUntil() != null && !(this.finalDate.equals(dados.dateUntil()))) {
			this.finalDate = dados.dateUntil();
		}
		if(dados.discount() >= 0 && !(this.discount == dados.discount())) {
			this.discount = dados.discount();
		}
		if(dados.items() != null) {
			this.value = this.valorItens(itens);
			this.valueTotal =  this.valorTotal(this.discount, this.valorItens(itens));
		}
		if(dados.observation() != null && !(this.observation.equals(dados.observation()))) {
			this.observation = dados.observation();
		}
		if(dados.annotations() != null && !(this.annotations.equals(dados.annotations()))) {
			this.annotations = dados.annotations();
		}
	}
	
	private double valorTotal(double desconto, double valor) {
		return (desconto == -1 || desconto == 0) ?
				this.value : this.value - ((valor * desconto) / 100);
		
	}
}


