package br.com.rosa.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryItem extends JpaRepository<Item, Long>{

    Item getReferenceByName(String teste2);

    boolean existsByCod(Long cod);

    @Query(value = "select * from itens where reference like :search% or cod like :search% or name like :search%", nativeQuery = true)
    List<Item> findAllByNameOrCodeOrReference(String search);

    boolean existsByName(String name);

    @Query(value = "select count(*) from itens i where i.categoria_id = :id", nativeQuery = true)
    Long existsByItemWithCategoryId(Long id);

    Item getReferenceByCod(Long id);

    @Query(value = "SELECT count(*) " +
            "FROM contract c " +
            "JOIN contract_itens i ON  c.id = i.contract_id " +
            "JOIN itens_contract ic ON ic.id = i.itens_id " +
            "WHERE ic.cod = :id",
            nativeQuery = true)
    Long findAllContractsWithItem(Long id);
}
