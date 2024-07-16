package br.com.rosa.domain.itemContract;

import br.com.rosa.domain.address.dto.DataAddress;
import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.client.dto.ClientRegister;
import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.dto.ContractItem;
import br.com.rosa.domain.contract.dto.ContractRegister;
import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.dto.RegisterItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest //indica para o spring que essa é uma classe de teste que vai testar um repository JPA
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RepositoryItemContractTest {

    @Autowired
    private RepositoryItemContract repositoryItemContractTest;

    @Autowired
    private RepositoryCategory repositoryCategoryTest;

    @Autowired
    private TestEntityManager entity;

    @Test
    @DisplayName("Should return is number of items for date")
    void quantityItemsDate() throws IOException {

        Set<ItemContract> listItemss = new HashSet<>();
        List<ContractItem> listItems = new ArrayList<>();

        var client = registerClient();

        var mockFile = returnImgFake();

        var category = new Category(1L, "test");

        if (category.getId() != null) {
            category = entity.merge(category);
        } else {
            entity.persist(category);
        }

        var registerItem = new RegisterItem(1L, "test", 10, 20, 1, "test");

        var item = new Item(registerItem, category.getId(), mockFile.getBytes());
        entity.persist(item);

        var contractRegister = new ContractRegister(
                client.getId(),
                LocalDate.of(2024, 7, 13),
                LocalDate.of(2024, 7, 14),
                5, "eu", listItems, null, null);

        var contract = new Contract(contractRegister, LocalDate.now(), listItemss);
        contract.setContractSituation(SituationContract.RESERVADO);

        var itemContract = new ItemContract(item, contract.getStartDate(), contract.getFinalDate(), contract.getContractSituation());
        listItemss.add(itemContract);
        entity.persist(contract);

        var amountItens = repositoryItemContractTest.quantityItemsDate(item.getId(),
                LocalDate.of(2024, 7, 13), LocalDate.of(2024, 7, 14),
                SituationContract.RESERVADO);

        assertEquals(amountItens, 1L);
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

    private MockMultipartFile returnImgFake() throws IOException {
        // Criação de uma imagem fake válida
        BufferedImage bufferedImage = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setColor(Color.BLUE);
        g2d.fillRect(0, 0, 200, 200);
        g2d.dispose();

        // Escrevemos a imagem em bytes
        ByteArrayOutputStream img = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", img);
        byte[] imageBytes = img.toByteArray();

        // Criação do MockMultipartFile com a imagem válida
        return new MockMultipartFile("file", "test.jpg", "image/jpeg", imageBytes);
    }
}