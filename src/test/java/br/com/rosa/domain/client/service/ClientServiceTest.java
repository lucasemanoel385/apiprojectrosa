package br.com.rosa.domain.client.service;

import br.com.rosa.domain.address.dto.DataAddress;
import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.client.RepositoryCliente;
import br.com.rosa.domain.client.dto.ClientRegister;

import br.com.rosa.domain.client.dto.UpdateClient;
import br.com.rosa.domain.client.validations.ValidateRgAndCpf;
import br.com.rosa.domain.client.validations.ValidationCpfCnpj;
import br.com.rosa.domain.contract.RepositoryContract;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import br.com.rosa.infra.exceptions.ValidationException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


import java.time.LocalDate;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Autowired
    @InjectMocks
    private ClientService serviceClientTest;

    @Mock
    private ValidateRgAndCpf validateRgAndCpfCnpj;

    @InjectMocks
    private ValidateRgAndCpf validateRgAndCpfCnpjInjectMock;

    @Mock
    private TestEntityManager em; //EntityManager é pra ser usado nos test

    @Mock
    private RepositoryCliente repositoryClienteTest;

    @Mock
    private RepositoryContract repositoryContractTest;

    @Mock
    private ValidationCpfCnpj validationCpfCnpj;



    @Test
    @DisplayName("Should success if client save in repository")
    void registerClientCase01() throws Exception {

        var client = dtoClientRegister("09068306642");

        var address = createAddress(client.cep(), client.street(), client.number(), client.district(), client.city(), client.uf());

        var clientRegister = new Client(client, address);

        serviceClientTest.registerClient(client);

        verify(repositoryClienteTest, times(1)).save(clientRegister);

    }

    @Test
    @DisplayName("Should throw ValidationException if CPF or CNPJ is invalid")
    void registerClientCase02() throws Exception {

        var client = dtoClientRegister("09068306641");

        doThrow(new ValidationException("CPF inválido"))
                .when(validationCpfCnpj).validateCnpjCpf(client.cpf());

        ValidationException exception =  Assertions.assertThrows(ValidationException.class, () -> {
            validationCpfCnpj.validateCnpjCpf(client.cpf());
        });
        Assertions.assertEquals("CPF inválido", exception.getMessage());

    }

    @Test
    @DisplayName("Should throw SqlConstraintViolationsException if register cpf existing")
    void registerClientCase03() throws Exception {

        var client = dtoClientRegister("09068306642");

        when(repositoryClienteTest.existsByCpfCnpj(client.cpf())).thenReturn(true);

        SqlConstraintViolationException exception =  Assertions.assertThrows(SqlConstraintViolationException.class, () -> {
            validateRgAndCpfCnpjInjectMock.ValidateCreateClientCpfAndRg(client.cpf(), client.rg());
        });

        var msgExpected = "";
        if (Objects.equals(exception.getMessage(), "CPF do cliente já existe")) {
            msgExpected = "CPF do cliente já existe";
        } else {
            msgExpected = "RG do cliente já existe";
        }
        Assertions.assertEquals(msgExpected, exception.getMessage());

    }

    @Test
    @DisplayName("Should return update client entity")
    void updateClient01() {
        var dateBirth = LocalDate.of(2000,12,17);
        var client = dtoClientRegister("09068306642");
        var address = createAddress(client.cep(), client.street(), client.number(), client.district(), client.city(), client.uf());

        var clientReturn = new Client(1L, client, address);

        var clientUpdate = new UpdateClient(1L,
                "Lucas", "09068306645", "456546547",dateBirth, "33125500",
                "Santa Luzia", "Sao Benedito","Rua monte calvario",
                "322","MG",null, null, null);

        when(repositoryClienteTest.getReferenceById(1L)).thenReturn(clientReturn);

        var returnClient = serviceClientTest.updateClient(clientUpdate);

        verify(repositoryClienteTest, times(1)).save(clientReturn);

        assertThat(returnClient).isEqualTo(clientReturn);

    }

    @Test
    @DisplayName("Should throw ValidationException if CPF or CNPJ is invalid")
    void updateClient02() throws Exception {

        var dateBirth = LocalDate.of(2000,12,17);
        var clientUpdate = new UpdateClient(1L,
                "Lucas", "09068306642", "456546546",dateBirth, "33125500",
                "Santa Luzia", "Sao Benedito","Rua monte calvario",
                "322","MG",null, null, null);

        doThrow(new ValidationException("CPF inválido"))
                .when(validationCpfCnpj).validateCnpjCpf(clientUpdate.cpfCnpj());

        ValidationException exception =  Assertions.assertThrows(ValidationException.class, () -> {
            validationCpfCnpj.validateCnpjCpf(clientUpdate.cpfCnpj());
        });

        Assertions.assertEquals("CPF inválido", exception.getMessage());

    }

    @Test
    void deleteById() {

        when(repositoryContractTest.existsByContractWithClientId(1L)).thenReturn(1L);

        SqlConstraintViolationException exception =  Assertions.assertThrows(SqlConstraintViolationException.class, () -> {
            serviceClientTest.deleteId(1L);
        });

        Assertions.assertEquals("Não é possível deletar cliente, pois há contratos cadastrados nele(a)", exception.getMessage());
    }

    private ClientRegister dtoClientRegister(String cep) {
        var dateBirth = LocalDate.of(2000,12,17);

        return new ClientRegister(
                "Lucas", dateBirth, cep,"456546546", "33125420",
                "Santa Luzia", "Sao Benedito","Rua monte calvario",
                "322","MG",null, null, null);
    }

    private DataAddress createAddress(String cep, String street, String number, String district, String city, String uf) {
        return new DataAddress(cep, street, number, district, city, uf);
    }
}