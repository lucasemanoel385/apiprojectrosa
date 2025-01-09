package br.com.rosa.domain.contract;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

	@Modifying
	@Query(value = "delete FROM contract where date_contract <= :dateNow AND contract_situation = 'ORCAMENTO'", nativeQuery = true)
	void deleteContractsBudgetsWithinSixMonthsAgo(String dateNow);

	@Modifying
	@Query(value = "delete FROM contract where final_date <= :dateNow AND contract_situation = 'RESERVADO'", nativeQuery = true)
	void deleteContractsReservationsWithinOneYearAgo(String dateNow);

	/*@Query(value = "SELECT \n" +
			"    i.contract_id\n" +
			"FROM \n" +
			"    contract_itens i\n" +
			"JOIN \n" +
			"    itens_contract ic ON ic.id = i.itens_id\n" +
			"WHERE \n" +
			"    ic.contract_situation = 'RESERVADO' and ic.cod = :cod and ic.start_date >= :dateNow", nativeQuery = true)
	List<Long> getItemsContractId(Long cod, String dateNow);*/


	@Query(value = "SELECT \n" +
			"    i.contract_id\n" +
			"FROM \n" +
			"    contract_itens i\n" +
			"JOIN \n" +
			"    itens_contract ic ON ic.id = i.itens_id\n" +
			"WHERE \n" +
			"    ic.contract_situation = 'RESERVADO' and ic.cod = :cod", nativeQuery = true)
	List<Long> getItemsContractId(Long cod);
}
