package br.com.rosa.domain.categoryItem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryCategory extends JpaRepository<Category, Long>{

    boolean existsByName(String s);

    Category getReferenceByName(String category);

    @Query(value = "select * from categorys where name like :search%", nativeQuery = true)
    Page<Category> findAllByName(String search , Pageable page);
}
