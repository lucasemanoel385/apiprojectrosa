package br.com.rosa.domain.itemContract;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.itemContract.dto.ItemContratoCadastro;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "itens_contract")
@Entity(name = "ItemContract")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "cod")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //você precisa ignorar os relacionamento lazy do hibernate porque eles vem inicialmente vazios e o jackson vai tentar fazer o parse dele pra json/xml
public class ItemContract {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long cod;
	private String name;
	private double valueItemContract;
	private double replacementValue;
	private Long quantity;
	private double valueTotalItem;
	private LocalDate startDate;
	private LocalDate finalDate;
	@Enumerated(EnumType.STRING)
	private SituationContract contractSituation;
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "categoria_id")
//	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//	private Categoria categoria;
	
	public ItemContract(ItemContratoCadastro dados) {

		this.name = dados.nome();
		this.valueItemContract = dados.valor();
		this.quantity = (long) dados.quantidade();
		this.contractSituation = dados.situacaoContrato();
	}
	
	public ItemContract(int id) {
		this.id = (long) id;
	}
	
	public ItemContract(Long id) {
		this.id = id;
	}
	
	public ItemContract(Item item, double valueItem,LocalDate dataInicio, LocalDate dataFinal, SituationContract situacaoContrato) {
		this.cod = item.getCod();
		this.name = item.getName();
		this.valueItemContract = valueItem;
		this.replacementValue = item.getReplacementValue();
		this.quantity = item.getQuantity();
		this.valueTotalItem = valueItem * item.getQuantity();
		this.startDate = dataInicio;
		this.finalDate = dataFinal;
		this.contractSituation = situacaoContrato;
	}

	public ItemContract(ItemContract item) {

		this.name = item.getName();
		this.valueItemContract = item.getValueItemContract();
		this.quantity = item.getQuantity();
	}


	public ItemContract(String nome2, double valor2, int quantidade2) {
		this.name = nome2;
		this.valueItemContract = valor2;
		this.quantity = (long) quantidade2;
	}
}
