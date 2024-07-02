package br.com.rosa.domain.contract.dto;

import br.com.rosa.domain.paymentContract.Payment;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record RegisterPayment(
        @Valid
        List<RegisterDataPayment> payments
) {
}
