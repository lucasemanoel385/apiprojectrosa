package br.com.rosa.domain.scheduling.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record SchedulingRegister(
		@NotNull
		String name,
		String description, 
		@NotNull
		String time,
		@NotNull
		LocalDate dateScheduling) {

}
