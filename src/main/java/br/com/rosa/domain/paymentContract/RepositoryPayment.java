package br.com.rosa.domain.paymentContract;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RepositoryPayment extends JpaRepository<Payment, Long> {

    @Query(value = "select coalesce(sum(p.payment_value), 0) from payment p where p.date_payment LIKE :month%", nativeQuery = true)
    double sumPaymentContainsMonth(String month);

    @Query(value = "select coalesce(sum(p.payment_value), 0) from payment p where p.date_payment LIKE :year%", nativeQuery = true)
    double sumPaymentContainsYear(String year);

}
