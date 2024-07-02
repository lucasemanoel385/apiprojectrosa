package br.com.rosa.domain.itemContract;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import br.com.rosa.domain.contract.enunm.SituacaoContrato;

public interface RepositoryItemContrato extends JpaRepository<ItemContract, Long>{

	@Query("""
			select coalesce(sum(i.amount), 0)
			from ItemContract
			i where i.idItem = :id and i.finalDate >= :dataInicio and i.startDate <= :dataFinal and i.contractSituation = :situacaoContrato
			""")
	Long quantityItemsDate(Long id ,LocalDate dataInicio, LocalDate dataFinal, SituacaoContrato situacaoContrato);

	@Modifying
	@Query(value = "DELETE FROM itens_contract WHERE id = :id", nativeQuery = true)
	void deleteById(Long id);
}
