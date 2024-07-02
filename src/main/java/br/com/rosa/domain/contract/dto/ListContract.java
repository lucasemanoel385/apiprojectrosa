package br.com.rosa.domain.contract.dto;


import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.enunm.SituacaoContrato;


import java.time.LocalDate;


public record ListContract(
		Long id,
		String cpf,
		String nameClient,
		LocalDate dateReserve,
		LocalDate dateFinal,
		String seller,
		SituacaoContrato situation) {
	public ListContract(Contract contract) {
		this(contract.getId(), contract.getClient().getCpfCnpj(), contract.getClient().getNameReason(), contract.getStartDate(), contract.getFinalDate(),
				contract.getFuncionario().getSeller(), contract.getContractSituation());
	}
}
