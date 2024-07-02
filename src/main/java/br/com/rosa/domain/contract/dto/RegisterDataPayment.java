package br.com.rosa.domain.contract.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RegisterDataPayment(
        @NotNull
        double paymentValue,
        @NotNull
        LocalDate datePayment
) {
}
