package br.com.rosa.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryItem extends JpaRepository<Item, Long>{

    Item getReferenceByName(String teste2);

    boolean existsByCod(Long cod);

    @Query(value = "select * from itens where reference = :search or cod = :search or name like :search%", nativeQuery = true)
    Page<Item> findAllByNameOrCodeOrReference(Pageable page, String search);

    @Query(value = "select * from itens where reference = :search", nativeQuery = true)
    Page<Item> findAllByReference(Pageable page, String search);

    @Query(value = "select * from itens where cod = :search", nativeQuery = true)
    Page<Item> findAllByCode(Pageable page, String search);

    @Query(value = "select * from itens where name like :search%", nativeQuery = true)
    Page<Item> findAllByName(Pageable page, String search);

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
