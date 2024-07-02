package br.com.rosa.domain.task.dto;

import jakarta.validation.constraints.NotNull;

public record TaskRegister(
		@NotNull
		String description
		) {

}
