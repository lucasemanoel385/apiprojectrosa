package br.com.rosa.domain.contract;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryContract extends JpaRepository<Contract, Long>{

	@Query(value = "select * from contract where start_date = :dataInicio and final_date = :dataFinal", nativeQuery = true)
	List<Contract> findAllData(LocalDate dataInicio, LocalDate dataFinal);

	@Query(value = "select c.id, c.client_id, c.date_contract, c.start_date, c.final_date, c.discount, c.value, \n" +
			"c.value_total, c.contract_situation, c.seller, c.observation, c.annotations\n" +
			"from contract c LEFT JOIN client cl ON c.client_id = cl.id where cl.name_reason like :search% or cl.cpf_cnpj like :search% or c.id like :search%", nativeQuery = true)
    Page<Contract> findAllByIdOrByClientNameOrByClientCpf(String search, Pageable page);

	@Query(value = "select * from contract", nativeQuery = true)
	Page<Contract> findAll(Pageable page);

	@Query(value = "select * from contract where start_date like :month% && contract_situation = 'RESERVADO'", nativeQuery = true)
    List<Contract> findAllByStartDate(String month);

	/*@Query(value = "WITH MonthDataReserve AS (\n" +
			"    select coalesce(sum(value_paid), 0) as valuePaidMonthReserve, coalesce(sum(value_total), 0) as valueTotalReserve ,count(*) as amountContractMonthReserve\n" +
			"    from contract\n" +
			"    where final_date like '2024-05%' and contract_situation = 'RESERVADO'\n" +
			"),\n" +
			"YearDataReserve AS (\n" +
			"    select coalesce(sum(value_paid), 0) as valuePaidYearReserve, count(*) as amountContractYearReserve\n" +
			"    from contract\n" +
			"    where final_date like '2024%' and contract_situation = 'RESERVADO'\n" +
			"),\n" +
			"MonthData AS (\n" +
			"   select count(*) as amountContractMonth\n" +
			"    from contract\n" +
			"    where final_date like '2024-05%'\n" +
			"),\n" +
			"YearData AS (\n" +
			"   select count(*) as amountContractYear\n" +
			"    from contract\n" +
			"    where final_date like '2024-05%'\n" +
			")\n" +
			"SELECT \n" +
			"    MonthDataReserve.valuePaidMonthReserve,\n" +
			"    MonthDataReserve.valueTotalReserve,\n" +
			"    MonthDataReserve.amountContractMonthReserve, \n" +
			"    YearDataReserve.valuePaidYearReserve, \n" +
			"    YearDataReserve.amountContractYearReserve,\n" +
			"    MonthData.amountContractMonth,\n" +
			"    YearData.amountContractYear\n" +
			"FROM MonthDataReserve, YearDataReserve, MonthData, YearData", nativeQuery = true)
	DataReserveYear t(String month, String year);*/

	/*@Query("SELECT NEW br.com.rosa.domain.contract.dto.DataAnalysisContract(" +
			"   COALESCE(SUM(CASE WHEN c.contractSituation = 'RESERVADO' AND FUNCTION('YEAR', c.finalDate) = :year AND FUNCTION('MONTH', c.finalDate) = :month THEN c.valuePaid ELSE 0 END), 0), " +
			"   COALESCE(SUM(CASE WHEN c.contractSituation = 'RESERVADO' AND FUNCTION('YEAR', c.finalDate) = :year AND FUNCTION('MONTH', c.finalDate) = :month THEN c.valueTotal ELSE 0 END), 0), " +
			"   COALESCE(COUNT(CASE WHEN c.contractSituation = 'RESERVADO' AND FUNCTION('YEAR', c.finalDate) = :year AND FUNCTION('MONTH', c.finalDate) = :month THEN c.id ELSE NULL END), 0), " +
			"   COALESCE(SUM(CASE WHEN c.contractSituation = 'RESERVADO' AND FUNCTION('YEAR', c.finalDate) = :year THEN c.valuePaid ELSE 0 END), 0), " +
			"   COALESCE(COUNT(CASE WHEN c.contractSituation = 'RESERVADO' AND FUNCTION('YEAR', c.finalDate) = :year THEN c.id ELSE NULL END), 0), " +
			"   COALESCE(COUNT(CASE WHEN FUNCTION('YEAR', c.finalDate) = :year AND FUNCTION('MONTH', c.finalDate) = :month THEN c.id ELSE NULL END), 0), " +
			"   COALESCE(COUNT(CASE WHEN FUNCTION('YEAR', c.finalDate) = :year THEN c.id ELSE NULL END), 0)" +
			") " +
			"FROM Contract c " +
			"WHERE (FUNCTION('YEAR', c.finalDate) = :year AND FUNCTION('MONTH', c.finalDate) = :month) " +
			"   OR FUNCTION('YEAR', c.finalDate) = :year")
	DataAnalysisContract dataForAnalysis(@Param("month") String month, @Param("year") String year);*/

	@Query(value = "   select count(*)\n" +
			"    from contract\n" +
			"    where date_contract like :month% and contract_situation = 'RESERVADO'", nativeQuery = true)
	Long dataContractByMonthReserve(String month);

	@Query(value = "    select coalesce(sum(value_paid), 0) as valuePaidYearReserve, count(*) as amountContractYearReserve\n" +
			"    from contract\n" +
			"    where date_contract like :year% and contract_situation = 'RESERVADO'", nativeQuery = true)
	double dataContractReserveByYear(String year);

	@Query(value = "   select count(*) as amountContractMonth\n" +
			"    from contract\n" +
			"    where date_contract like :month%", nativeQuery = true)
	Long dataContractByMonth(String month);

	@Query(value = "   select count(*) as amountContractYear\n" +
			"    from contract\n" +
			"    where date_contract like :year%", nativeQuery = true)
	Long dataContractByYear(String year);

	@Query(value = "select count(*) from contract c where c.client_id = :id", nativeQuery = true)
    Long existsByContractWithClientId(Long id);

	@Query(value = "select c.seller, coalesce(sum(p.payment_value), 0) as value from contract c LEFT JOIN payment p ON c.id = p.contract_id where \n" +
			"p.date_payment LIKE :month% and c.contract_situation = 'RESERVADO' GROUP BY seller", nativeQuery = true)
	List<Object[]> findAllSellerAndValuePaymentOfMonth(String month);
}
