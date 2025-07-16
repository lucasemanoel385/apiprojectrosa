package br.com.rosa.domain.client;


import static org.assertj.core.api.Assertions.assertThat;
import br.com.rosa.domain.address.dto.DataAddress;
import br.com.rosa.domain.client.dto.ClientRegister;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@DataJpaTest //indica para o spring que essa é uma classe de teste que vai testar um repository JPA
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RepositoryClienteTest {

    @Autowired
    private RepositoryCliente clientRepository;

    @Autowired
    private TestEntityManager em; //EntityManager é pra ser usado nos test


    @Test
    @DisplayName("Find all with name or cpf")
    void findAllByNameRasonAndcpfCnpj01() {

        List<Client> listClient = new ArrayList<>();
        listClient.add(registerClient());
        Pageable page = PageRequest.of(0, 5);

        int start = (int) page.getOffset();
        int end = Math.min((start + page.getPageSize()), listClient.size());

        Page<Client> listPageClient = new PageImpl<>(listClient.subList(start, end), page, listClient.size());

        var clientsFound = clientRepository.findAllByNameRasonAndcpfCnpj("09068306642", page);

        assertThat(clientsFound).isEqualTo(listPageClient);
    }

    @Test
    @DisplayName("If name or cpf not contains return empty")
    void findAllByNameRasonAndcpfCnpj02() {

        Pageable page = PageRequest.of(0, 5);

        registerClient();

        var clientsFound = clientRepository.findAllByNameRasonAndcpfCnpj("09068306641", page);

        assertThat(clientsFound).isEmpty();
    }

    private Client registerClient() {
        var dateBirth = LocalDate.of(2000, 12,17);

        var clientRegister = new ClientRegister(
                "Lucas", dateBirth, "09068306642",
                "456456546456", "33125420", "Santa Luzia", "São Benedito",
                "", "Rua monte calvario","322", "MG", null, null,null);

        var address = new DataAddress("33125420", "Rua monte calvario",
                "322", "São benedito", "", "Santa Luzia","MG");

        var client = new Client(clientRegister, address);

        em.persist(client);

        return client;
    }

}