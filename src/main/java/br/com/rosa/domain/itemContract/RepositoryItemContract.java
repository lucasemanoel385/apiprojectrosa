package br.com.rosa.domain.itemContract;

import java.time.LocalDate;
import java.util.List;

import br.com.rosa.domain.itemContract.dto.ItemsWithContractIdDTO;
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

	@Query(value = "select * from itens_contract where start_date between :dateNow and :datePlusSeven and contract_situation = 'RESERVADO'", nativeQuery = true)
	List<ItemContract> findAllItemsBetweenDate(String dateNow, String datePlusSeven);

	/*@Query("""
			select i
			from ItemContract
			i where (i.reference = :reference or i.name like :reference%) and i.finalDate >= :dateFirst and i.startDate <= :dateEnd and i.contractSituation = :situationContract
			""")
	List<ItemContract> findAllItemsReservedInDate(String reference, LocalDate dateFirst, LocalDate dateEnd, SituationContract situationContract);

	@Query("""
			select i
			from ItemContract
			i where (i.reference = :reference or i.name like :reference%) AND (i.finalDate >= :now OR i.startDate >= :now) AND i.contractSituation = :situationContract
			""")
	List<ItemContract> findAllItemsReservedNotDate(String reference, LocalDate now, SituationContract situationContract);*/

	@Query(value = """
    SELECT 
        i.id as id,
        i.cod as cod,
        i.reference as reference,
        i.name as name,
        i.quantity as quantity,
        i.start_date as startDate,
        i.final_date as finalDate,
        i.contract_situation as contractSituation,
        c.id as contractId,
        cl.name_reason as nameClient
    FROM itens_contract i
    JOIN contract_itens ci ON ci.itens_id = i.id
    JOIN contract c ON ci.contract_id = c.id
    JOIN client cl ON c.client_id = cl.id
    WHERE 
        (i.reference = :reference OR i.name LIKE CONCAT(:reference, '%'))
     	AND (i.final_date >= :now OR i.start_date >= :now)
        AND i.contract_situation = :situationContract
	""", nativeQuery = true)
	List<ItemsWithContractIdDTO> findFilteredItensWithContractIdNotDate(
			String reference,
			LocalDate now,
			String situationContract
	);

	@Query(value = """
    SELECT 
        i.id as id,
        i.cod as cod,
        i.reference as reference,
        i.name as name,
        i.quantity as quantity,
        i.start_date as startDate,
        i.final_date as finalDate,
        i.contract_situation as contractSituation,
        c.id as contractId,
        cl.name_reason as nameClient
    FROM itens_contract i
    JOIN contract_itens ci ON ci.itens_id = i.id
    JOIN contract c ON ci.contract_id = c.id
    JOIN client cl ON c.client_id = cl.id
    WHERE 
        (i.reference = :reference OR i.name LIKE CONCAT(:reference, '%'))
        AND i.final_date >= :dateFirst
        AND i.start_date <= :dateEnd
        AND i.contract_situation = :situationContract
	""", nativeQuery = true)
	List<ItemsWithContractIdDTO> findAllItemsReservedInDateWithContract(
			String reference,
			LocalDate dateFirst,
			LocalDate dateEnd,
			String situationContract
	);
}
