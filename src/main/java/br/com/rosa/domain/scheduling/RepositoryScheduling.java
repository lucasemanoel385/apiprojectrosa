package br.com.rosa.domain.scheduling;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryScheduling extends JpaRepository<Scheduling, Long> {

	@Query(value = "select * from scheduling where date_scheduling = :dateDay", nativeQuery = true)
	List<Scheduling> findAllByDateScheduling(String dateDay);

	@Modifying
	@Query("DELETE FROM Scheduling s WHERE s.dateScheduling < :dateNow")
    void deleteAllSchedulingAfterOfData(LocalDate dateNow);
}
