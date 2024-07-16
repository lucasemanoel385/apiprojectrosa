package br.com.rosa.domain.contract;

import br.com.rosa.domain.address.dto.DataAddress;
import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.client.RepositoryCliente;
import br.com.rosa.domain.client.dto.ClientRegister;
import br.com.rosa.domain.contract.dto.ContractItem;
import br.com.rosa.domain.contract.dto.ContractRegister;
import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.itemContract.ItemContract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //indica para o spring que essa é uma classe de teste que vai testar um repository JPA
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RepositoryContractTest {

    @Autowired
    private RepositoryContract repositoryContract;

    @Autowired
    private TestEntityManager entity;

    @Test
    void findAllData() {

        var start = LocalDate.of(2024, 7, 13);
        var end = LocalDate.of(2024, 7, 14);

        registerContract();

        var returnFindAllData = repositoryContract.findAllData(start, end);

        assertThat(returnFindAllData.size()).isEqualTo(1);

    }

    @Test
    void findAllByIdOrByClientNameOrByClientCpf() {

        Pageable page = PageRequest.of(0, 5);

        registerContract();

        var returnFindAllFilter = repositoryContract.findAllByIdOrByClientNameOrByClientCpf("09068306642", page);

        assertThat(returnFindAllFilter.getNumberOfElements()).isEqualTo(1);

    }

    @Test
    @DisplayName("Should return contracts of month")
    void findAllByStartDate() {

        registerContract();

        var returnFindAllMonth = repositoryContract.findAllByStartDate("2024-07");

        assertThat(returnFindAllMonth.size()).isEqualTo(1);
    }

    private Contract registerContract() {
        List<ContractItem> listItems = new ArrayList<>();

        Set<ItemContract> listItemss = new HashSet<>();

        var client = registerClient();

        var contractRegister = new ContractRegister(
                client.getId(),
                LocalDate.of(2024, 7, 13),
                LocalDate.of(2024, 7, 14),
                5, "eu", listItems, null, null);

        var contract = new Contract(contractRegister, LocalDate.now(), listItemss);
        contract.setContractSituation(SituationContract.RESERVADO);
        entity.persist(contract);

        return  contract;
    }

    private Client registerClient() {
        var dateBirth = LocalDate.of(2000, 12,17);

        var clientRegister = new ClientRegister(
                "Lucas", dateBirth, "09068306642",
                "456456546456", "33125420", "Santa Luzia", "São Benedito",
                "Rua monte calvario", "322", "MG", null, null,null);

        var address = new DataAddress("33125420", "Rua monte calvario",
                "322", "São benedito", "Santa Luzia", "MG");

        var client = new Client(clientRegister, address);
        entity.persist(client);

        return client;
    }
}