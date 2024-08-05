package br.com.rosa.domain.contract.service;

import br.com.rosa.domain.contract.Contract;
import br.com.rosa.domain.contract.RepositoryContract;
import br.com.rosa.domain.contract.dto.ContractItem;
import br.com.rosa.domain.contract.dto.ContractRegister;
import br.com.rosa.domain.contract.dto.UpdateContract;
import br.com.rosa.domain.contract.validations.ValidateAvailableItemByDate;
import br.com.rosa.domain.contract.validations.ValidateContractRent;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.item.dto.RegisterItem;
import br.com.rosa.domain.itemContract.ItemContract;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import br.com.rosa.infra.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Autowired
    @InjectMocks
    private ContractService serviceContract;

    @Mock
    private RepositoryContract repositoryContractTest;

    @Mock
    private RepositoryItem repositoryItemTest;

    @Mock
    private RepositoryItemContract repositoryItemContractTest;

    @Mock
    private List<ValidateContractRent> validate;

    @InjectMocks
    private ValidateAvailableItemByDate validateItemByDateTest;


    @Test
    @DisplayName("Should success if item save in repository")
    void registerContract01() {

        List<ContractItem> listItems = new ArrayList<>();

        Set<ItemContract> listItemss = new HashSet<>();

        var contractRegister = dtoRegisterContract(listItems);

        var contract = new Contract(contractRegister, LocalDate.now(), listItemss);

        serviceContract.registerContract(contractRegister);

        verify(repositoryContractTest,times(1)).save(contract);

    }

    @Test
    @DisplayName("Should throw ValidationException if items duplicate")
    void registerContract02() throws IOException {

        List<ContractItem> listItems = new ArrayList<>();
        var item1 = new ContractItem(1L, 1L, 10);
        var item2 = new ContractItem(1L, 2L, 10);
        listItems.add(item1);
        listItems.add(item2);

        var mockFile = returnImgFake();
        var registerItem = new RegisterItem(1L, "teste", 10, 20, 1, "teste");
        var item = new Item(registerItem, 1L, mockFile.getBytes());

        var contractRegister = dtoRegisterContract(listItems);

        when(repositoryItemTest.getReferenceById(item1.getId())).thenReturn(item);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            serviceContract.registerContract(contractRegister);
        });

        assertThat("Itens iguais, favor remover o item duplicado.").isEqualTo(exception.getMessage());

    }

    @Test
    @DisplayName("Should throw ValidationException if amount > stock")
    void registerContract03() throws IOException {

        Set<ItemContract> listItemss = new HashSet<>();
        List<ContractItem> listItems = new ArrayList<>();

        var contractRegister = dtoRegisterContract(listItems);

        var contract = registerContract(contractRegister);

        var mockFile = returnImgFake();

        var registerItem = new RegisterItem(1L, "teste", 10, 20, 2, "teste");

        var item = new Item(registerItem, 1L, mockFile.getBytes());

        var registerItem1 = new RegisterItem(1L, "teste", 10, 20, 1, "teste");

        var item1 = new Item(registerItem1, 1L, mockFile.getBytes());

        var itemContract = new ItemContract(item, contract.getStartDate(), contract.getFinalDate(), contract.getContractSituation());

        listItemss.add(itemContract);

        when(repositoryItemContractTest.quantityItemsDate(any(), any(), any(), any())).thenReturn(0L);

        when(repositoryItemTest.getReferenceById(any())).thenReturn(item1);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            serviceContract.registerContract(contractRegister);
            validateItemByDateTest.validate(listItemss);
        });

        verify(repositoryContractTest,times(1)).save(contract);

        assertEquals("Unidades do item: teste ultrapassada. Estoque total: 1",
                exception.getMessage());

    }

    @Test
    @DisplayName("Should throw Validation Exception if lack of stock on date")
    void registerContract04() throws IOException {

        Set<ItemContract> listItemss = new HashSet<>();
        List<ContractItem> listItems = new ArrayList<>();

        var contractRegister = dtoRegisterContract(listItems);

        var contract = registerContract(contractRegister);

        var mockFile = returnImgFake();

        var registerItem = new RegisterItem(1L, "teste", 10, 20, 2, "teste");

        var item = new Item(registerItem, 1L, mockFile.getBytes());

        var itemContract = new ItemContract(item, contract.getStartDate(), contract.getFinalDate(), contract.getContractSituation());

        listItemss.add(itemContract);

        when(repositoryItemContractTest.quantityItemsDate(any(), any(), any(), any())).thenReturn(1L);

        when(repositoryItemTest.getReferenceById(any())).thenReturn(item);

        ValidationException exception = Assertions.assertThrows(ValidationException.class, () -> {
            serviceContract.registerContract(contractRegister);
            validateItemByDateTest.validate(listItemss);
        });

        verify(repositoryContractTest,times(1)).save(contract);

        assertEquals("Falta de estoque na data especifica. (Cód do item: 1 - Nome do item: teste - Estoque total de unidades: 2)",
                exception.getMessage());

    }

    @Test
    void changeContract() {

        List<ContractItem> listItems = new ArrayList<>();

        Set<ItemContract> listItemss = new HashSet<>();

        var contractRegister = dtoRegisterContract(listItems);

        var contract = registerContract(contractRegister);

        var updateContract = new UpdateContract(1L, 1L, "986537693", LocalDate.of(2024, 7, 13),
                LocalDate.of(2024, 07, 14), listItems, 0, "eu", null, null);

        when(repositoryContractTest.getReferenceById(1L)).thenReturn(contract);

        serviceContract.changeContract(updateContract);
        contract.setItens(listItemss);
        verify(repositoryContractTest,times(1)).save(contract);

        contract.updateAtrb(updateContract, listItemss);
        verify(repositoryContractTest,times(1)).save(contract);
    }

    @Test
    void changeSituationContract() {
    }

    private ContractRegister dtoRegisterContract(List<ContractItem> listItems) {

        return new ContractRegister(
                1L,
                LocalDate.of(2024, 7, 13),
                LocalDate.of(2024, 7, 14),
                5, "eu", listItems, null, null);

    }

    private Contract registerContract(ContractRegister contractRegister) {

        Set<ItemContract> listItemss = new HashSet<>();

        return new Contract(contractRegister, LocalDate.now(), listItemss);

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