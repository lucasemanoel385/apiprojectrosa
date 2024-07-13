package br.com.rosa.domain.itemContract.dto;

import br.com.rosa.domain.contract.enunm.SituationContract;

public record ItemContratoCadastro(

		String nome, 
		double valor,
		int quantidade, 
		SituationContract situacaoContrato) {

}
