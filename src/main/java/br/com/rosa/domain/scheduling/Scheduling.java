package br.com.rosa.domain.scheduling;

import java.time.LocalDate;

import br.com.rosa.domain.scheduling.dto.SchedulingRegister;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Scheduling {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String description;
	private String time;
	private LocalDate dateScheduling;
	
	
	public Scheduling(SchedulingRegister data) {
		this.name = data.name();
		this.description = data.description();
		this.time = data.time();
		this.dateScheduling = data.dateScheduling();
	}

}
