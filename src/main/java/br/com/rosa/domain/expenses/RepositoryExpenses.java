package br.com.rosa.domain.expenses;

import br.com.rosa.domain.contract.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositoryExpenses extends JpaRepository<Expenses, Long> {

    @Query(value = "select * from expenses where date like :month%;", nativeQuery = true)
    List<Expenses> findAllContainsMonth(String month);

    @Query(value = "select coalesce(sum(value), 0) from expenses where date like :year%", nativeQuery = true)
    double sumValueContainsYear(String year);

    @Query(value = "select coalesce(sum(value), 0) from expenses where date like :month%", nativeQuery = true)
    double sumValueContainsMonth(String month);

}
