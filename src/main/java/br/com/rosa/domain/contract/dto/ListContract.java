package br.com.rosa.domain.contract.dto;


import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.enunm.SituationContract;


import java.time.LocalDate;


public record ListContract(
		Long id,
		String cpf,
		String nameClient,
		LocalDate dateReserve,
		LocalDate dateFinal,
		String seller,
		SituationContract situation) {
	public ListContract(Contract contract) {
		this(contract.getId(), contract.getClient().getCpfCnpj(), contract.getClient().getNameReason(), contract.getStartDate(), contract.getFinalDate(),
				contract.getSeller().getSeller(), contract.getContractSituation());
	}
}
