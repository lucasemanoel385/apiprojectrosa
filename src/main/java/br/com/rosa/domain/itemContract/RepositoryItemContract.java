package br.com.rosa.domain.itemContract;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.rosa.domain.contract.enunm.SituationContract;

public interface RepositoryItemContract extends JpaRepository<ItemContract, Long>{

	@Query("""
			select coalesce(sum(i.quantity), 0)
			from ItemContract
			i where i.cod = :id and i.finalDate >= :dateFirst and i.startDate <= :dateEnd and i.contractSituation = :situacaoContrato
			""")
	Long quantityItemsDate(Long id ,LocalDate dateFirst, LocalDate dateEnd, SituationContract situacaoContrato);

	@Modifying
	@Query(value = "DELETE FROM itens_contract WHERE id = :id", nativeQuery = true)
	void deleteById(Long id);

	@Query(value = "select * from itens_contract where final_date between :dateMinusSeven and :datePlusSeven and contract_situation = 'RESERVADO'", nativeQuery = true)
	List<ItemContract> findAllItemsBetweenDate(String dateMinusSeven, String datePlusSeven);
}
