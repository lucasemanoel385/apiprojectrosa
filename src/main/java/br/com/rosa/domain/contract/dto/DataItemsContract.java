package br.com.rosa.domain.contract.dto;

public record DataItemsContract(
		Long id,
		Long cod,
		String reference,
		String name,
		String url,
		Long amount,
		double value,
		double valueTotal,
		String valueReplacement
	) {
	public DataItemsContract(DataItemsContract data) {
		this(data.id, data.cod, data.reference(), data.name, data.url ,data.amount, data.value, data.valueTotal, data.valueReplacement);
	}

}
