package br.com.rosa.domain.client.service;

import br.com.rosa.domain.address.dto.DataAddress;
import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.client.RepositoryCliente;
import br.com.rosa.domain.client.dto.ClientRegister;
import br.com.rosa.domain.client.dto.UpdateClient;
import br.com.rosa.domain.client.validations.ValidateRgAndCpf;
import br.com.rosa.domain.contract.RepositoryContract;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    @Autowired
    private ValidateRgAndCpf validateRgAndCpfCnpj;

    @Autowired
    private RepositoryCliente repositoryCliente;

    @Autowired
    private RepositoryContract repositoryContract;

    public Client registerClient(@Valid ClientRegister data) {

        validateRgAndCpfCnpj.ValidateCreateClientCpfAndRg(data.cpf(), data.rg());

        var address = new DataAddress(data.cep(), data.street(), data.number(), data.district(), data.complement(),data.city(), data.uf());
        var client = new Client(data, address);

        repositoryCliente.save(client);

        return client;
    }

    public Client getClientId(Long id) {

        return repositoryCliente.getReferenceById(id);
    }

    public Client updateClient(UpdateClient data) {

        var client = repositoryCliente.getReferenceById(data.id());

        validateRgAndCpfCnpj.ValidateUpdateClientCpfAndRg(data, client);

        client.updateTheInformation(data);

        repositoryCliente.save(client);

        return client;
    }

    public void deleteId(Long id) {

        var contractsWithClient = repositoryContract.existsByContractWithClientId(id);

        if (contractsWithClient > 0) {
            throw new SqlConstraintViolationException("Não é possível deletar cliente, pois há contratos cadastrados nele(a)");
        }

        repositoryCliente.deleteById(id);

    }

    public Page<Client> filterClients(String search, Pageable page) {

        return repositoryCliente.findAllByNameRasonAndcpfCnpj(search, page);
    }

}
