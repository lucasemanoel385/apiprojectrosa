package br.com.rosa.domain.contract.dto;

import java.time.LocalDate;
import java.util.List;

import br.com.rosa.domain.client.Client;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.enunm.SituationContract;

import br.com.rosa.domain.paymentContract.Payment;

public record DataContract(
		Long id, 
		Client client,
		LocalDate dateContract,
		LocalDate dateOf,
		LocalDate dateUntil,
		LocalDate dateTrialDress,
		LocalDate dateEvent,
		List<Payment> payment,
		double value,
		double discount,
		double valueTotal,
		SituationContract contractSituation,
		String seller,
		String observation,
		String annotations,
		List<DataItemsContract> items

	) {
	public DataContract(Contract contract, List<DataItemsContract> items) {
		this(contract.getId(), contract.getClient(), contract.getDateContract(),contract.getStartDate(), contract.getFinalDate(),
				 contract.getDateTrialDress(), contract.getDateEvent(), contract.getPayment(), contract.getValue(), contract.getDiscount(), contract.getValueTotal(), contract.getContractSituation(),
				contract.getSeller().getSeller(), contract.getObservation(), contract.getAnnotations(), items);
	}

}
