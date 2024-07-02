package br.com.rosa.domain.scheduling;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryScheduling extends JpaRepository<Scheduling, Long> {

	@Query(value = "select * from scheduling where date_scheduling = :dateDay", nativeQuery = true)
	List<Scheduling> findAllByDateScheduling(String dateDay);
}
