package br.com.rosa.domain.contract.dto;

import br.com.rosa.domain.contract.enunm.SituationContract;
import jakarta.validation.constraints.NotNull;

public record UpdateSituationContract(
		@NotNull
		Long contractId,
		@NotNull
        SituationContract situationContract
		) {

}
