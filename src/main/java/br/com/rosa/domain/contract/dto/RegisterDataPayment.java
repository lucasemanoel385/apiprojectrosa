package br.com.rosa.domain.contract.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterDataPayment(
        @NotNull(message = "Valor não pode estar vazia")
        double paymentValue,
        @NotNull(message = "Data não pode estar vazia")
        LocalDate datePayment
) {
}
