package br.com.rosa.domain.paymentContract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Payment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private double paymentValue;
    private LocalDate datePayment;

    public Payment(double paymentValue, LocalDate datePayment) {
        this.paymentValue = paymentValue;
        this.datePayment = datePayment;
    }
}
