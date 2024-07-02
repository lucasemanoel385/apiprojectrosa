package br.com.rosa.domain.contract.dto;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;
import br.com.rosa.domain.itemContract.ItemContract;

import java.time.LocalDate;
import java.util.List;

public record DataItemsContract(
		Long id,
		Long cod,
		String name,
		Long amount,
		double value,
		double valueTotal,
		double valueReplacement,
		String imagem
	) {
	public DataItemsContract(DataItemsContract data) {
		this(data.id, data.cod, data.name, data.amount, data.value, data.valueTotal, data.valueReplacement,data.imagem);
	}

}
