package br.com.rosa.domain.contract.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ContractItem {
	
	private Long id;
	private Long amount;
	private double total;
	
	public ContractItem(Long id, Long amount, double total) {
		this.id = id;
		this.amount = amount;
		this.total = total;
	}
	

}
