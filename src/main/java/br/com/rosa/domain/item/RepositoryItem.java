package br.com.rosa.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RepositoryItem extends JpaRepository<Item, Long>{

    Item getReferenceByName(String teste2);

    boolean existsByCod(Long cod);

    @Query(value = "select * from itens where name like :search% or cod like :search%", nativeQuery = true)
    List<Item> findAllByNameOrCode(String search);

    boolean existsByName(String name);

    @Query(value = "select count(*) from itens i where i.categoria_id = :id", nativeQuery = true)
    Long existsByItemWithCategoryId(Long id);
}
