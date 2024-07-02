package br.com.rosa.domain.client.service;

import br.com.rosa.domain.address.dto.DataAddress;
import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.client.RepositoryCliente;
import br.com.rosa.domain.client.dto.ClientRegister;
import br.com.rosa.domain.client.dto.UpdateClient;
import br.com.rosa.domain.client.service.validations.ValidationCpfCnpj;
import br.com.rosa.domain.contract.RepositoryContrato;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ValidationCpfCnpj validationCpfCnpj;

    @Autowired
    private RepositoryCliente repositoryCliente;

    @Autowired
    private RepositoryContrato repositoryContract;

    public Client registerClient(ClientRegister data) {

        checkRgAndCpf(data.cpf(), data.rg());

        var addrees = new DataAddress(data.cep(), data.street(), data.number(), data.district(), data.city(), data.uf());
        var client = new Client(data, addrees);

        repositoryCliente.save(client);

        return client;
    }

    public Client updateClient(UpdateClient dados) {

        var client = repositoryCliente.getReferenceById(dados.id());

        client.atualizarInformacoes(dados);

        repositoryCliente.save(client);

        return client;
    }

    public Page<Client> filterClients(String search, Pageable page) {

        var clients = repositoryCliente.findAllByNameRasonAndcpfCnpj(search, page);

        return clients;
    }

    private void checkRgAndCpf(String cpf, String rg) {

        if (repositoryCliente.existsByCpfCnpj(cpf)) {
            throw new SqlConstraintViolationException("CPF já existente");
        }
        if (repositoryCliente.existsByRgStateRegistration(rg)) {
            throw new SqlConstraintViolationException("RG já existe");
        }

        validationCpfCnpj.validateCnpjCpf(cpf);
    }


    public void deleteId(Long id) {

        var contractsWithClient = repositoryContract.existsByContractWithClientId(id);

        if (contractsWithClient > 0) {
            throw new SqlConstraintViolationException("Não é possível deletar cliente, pois há contratos cadastrados nele(a)");
        }

        repositoryCliente.deleteById(id);

    }
}
