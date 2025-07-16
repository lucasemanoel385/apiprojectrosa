package br.com.rosa.domain.scheduling.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.rosa.domain.scheduling.RepositoryScheduling;
import br.com.rosa.domain.scheduling.Scheduling;
import br.com.rosa.domain.scheduling.dto.SchedulingRegister;
import br.com.rosa.infra.exceptions.ValidationException;

@Service
public class SchedulingService {
	
	@Autowired
	private RepositoryScheduling schedulingRepository;

	public Scheduling registerScheduling(SchedulingRegister data) {
		
		Scheduling scheduling = new Scheduling(data);

		var dateNow = LocalDate.now();

		var diffBetweenDays = ChronoUnit.DAYS.between(dateNow ,data.dateScheduling());

		if (diffBetweenDays > 30) {
			throw new ValidationException("Não é possivel agendar com 30 dias de antecendência");
		}

		if (diffBetweenDays < 0) {
			throw new ValidationException("Não é possivel agendar na data posterior");
		}
		
		schedulingRepository.save(scheduling);

		deleteItemsAfterOfDate();

		return scheduling;
	}

	public List<Scheduling> listDateScheduling(String dateDay) {

		return schedulingRepository.findAllByDateScheduling(dateDay);

	}

	public void deleteScheduling(Long id) {
		schedulingRepository.deleteById(id);
		
	}

	private void deleteItemsAfterOfDate() {
		schedulingRepository.deleteAllSchedulingAfterOfData(LocalDate.now());

	}
	
	

}
