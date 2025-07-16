package br.com.rosa.domain.client.validations;

import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.client.RepositoryCliente;
import br.com.rosa.domain.client.dto.UpdateClient;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidateRgAndCpf {

    @Autowired
    private RepositoryCliente repository;

    @Autowired
    private ValidationCpfCnpj validationCpfCnpj;

    public void ValidateUpdateClientCpfAndRg(UpdateClient data, Client client) {

        if (!(data.rgStateRegistration() == null || data.rgStateRegistration() == "")) {
            if (!data.rgStateRegistration().equals(client.getRgStateRegistration())
                    &&
                    repository.existsByRgStateRegistration(data.rgStateRegistration())) {
                throw new SqlConstraintViolationException("RG do cliente já existe");
            }
        }

        if (!data.cpfCnpj().equals(client.getCpfCnpj()) && repository.existsByCpfCnpj(data.cpfCnpj())) {
            throw new SqlConstraintViolationException("CPF do cliente já existe");
        }

        validationCpfCnpj.validateCnpjCpf(data.cpfCnpj());
    }

    public void ValidateCreateClientCpfAndRg(String cpf, String rg) {

        if (repository.existsByCpfCnpj(cpf)) {
            throw new SqlConstraintViolationException("CPF do cliente já existe");
        }

        if(!(rg == null || rg == "")) {
            if (repository.existsByRgStateRegistration(rg)) {
                throw new SqlConstraintViolationException("RG do cliente já existe");
            }

        }

        validationCpfCnpj.validateCnpjCpf(cpf);
    }
}
