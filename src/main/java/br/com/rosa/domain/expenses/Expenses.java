package br.com.rosa.domain.expenses;

import br.com.rosa.domain.expenses.dto.DataExepenses;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Expenses {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private double value;
	private LocalDate date;

	public Expenses(DataExepenses data) {
		this.description = data.description();
		this.value = data.value();
		this.date = data.date();
	}
}
