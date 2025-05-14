package br.com.rosa.domain.contract.validations;
import br.com.rosa.infra.exceptions.ValidationException;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
public class StartDateCannotGreaterThanFinalDate {

    public void startDateCannotGreaterThanFinalDate(LocalDate dateOf, LocalDate dateUntil) {

        if (dateOf.isAfter(dateUntil)) {
            throw new ValidationException("Data retirada maior que data devolução.");
        }

    }

}
