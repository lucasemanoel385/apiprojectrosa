package br.com.rosa.domain.contract.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.rosa.domain.client.Client;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;

import br.com.rosa.domain.itemContract.ItemContract;
import br.com.rosa.domain.paymentContract.Payment;

public record DadosContrato(
		Long id, 
		Client client,
		LocalDate dateOf,
		LocalDate dateUntil,
		List<Payment> payment,
		double value,
		double discount,
		double valueTotal,
		SituacaoContrato contractSituation,
		String seller,
		String observation,
		String annotations,
		List<DataItemsContract> items

	) {
	public DadosContrato(Contract contract, List<DataItemsContract> items) {
		this(contract.getId(), contract.getClient(), contract.getStartDate(), contract.getFinalDate(),
				 contract.getPayment(), contract.getValue(), contract.getDiscount(), contract.getValueTotal(), contract.getContractSituation(),
				contract.getFuncionario().getSeller(), contract.getObservation(), contract.getAnnotations(), items);
	}

}
