package br.com.rosa.domain.contract.dto;

public record DataItemsContract(
		Long id,
		Long cod,
		String reference,
		String name,
		Long amount,
		double value,
		double valueTotal,
		double valueReplacement,
		String imagem
	) {
	public DataItemsContract(DataItemsContract data) {
		this(data.id, data.cod, data.reference(), data.name, data.amount, data.value, data.valueTotal, data.valueReplacement,data.imagem);
	}

}
