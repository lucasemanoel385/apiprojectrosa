package br.com.rosa.domain.item;

import br.com.rosa.domain.item.dto.ItemSummaryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryItem extends JpaRepository<Item, Long>{

    Item getReferenceByName(String teste2);

    boolean existsByCod(Long cod);

    @Query(value = """
    SELECT i.cod, i.name, i.reference, i.replacement_value, i.quantity
    FROM itens i 
    WHERE i.reference = :search OR i.cod = :search OR i.name LIKE CONCAT(:search, '%')
    """,
            countQuery = """
    SELECT count(*) FROM itens i 
    WHERE i.reference = :search OR i.cod = :search OR i.name LIKE CONCAT(:search, '%')
    """,
            nativeQuery = true)
    Page<ItemSummaryDTO> findAllByNameOrCodeOrReference(Pageable page, String search);

    @Query("""
    SELECT i.cod AS cod, i.reference AS reference, i.name AS name, 
           i.replacementValue AS replacementValue, i.quantity AS quantity, 
           i.category AS category
    FROM Item i
    WHERE i.reference = :search
    """)
    Page<ItemSummaryDTO> findAllByReference(Pageable page, String search);

    @Query("""
    SELECT i.cod AS cod, i.reference AS reference, i.name AS name, 
           i.replacementValue AS replacementValue, i.quantity AS quantity, 
           i.category AS category
    FROM Item i
    WHERE i.cod = :search
    """)
    Page<ItemSummaryDTO> findAllByCode(Pageable page, String search);

    @Query("""
    SELECT i.cod AS cod, i.reference AS reference, i.name AS name, 
           i.replacementValue AS replacementValue, i.quantity AS quantity, 
           i.category AS category
    FROM Item i
    WHERE i.name LIKE CONCAT(:search, '%')
    """)
    Page<ItemSummaryDTO> findAllByName(Pageable page, String search);

    @Query("""
    SELECT i.cod AS cod, i.reference AS reference, i.name AS name,
           i.replacementValue AS replacementValue, i.quantity AS quantity,
           i.category AS category
    FROM Item i
    """)
    Page<ItemSummaryDTO> findAllItems(Pageable page);

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

    @Query("SELECT i.img FROM Item i WHERE i.cod = :cod")
    byte[] getImgByCod(Long cod);
}
