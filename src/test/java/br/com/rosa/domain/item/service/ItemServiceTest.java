package br.com.rosa.domain.item.service;

import br.com.rosa.domain.TransformAndResizeImage;
import br.com.rosa.domain.categoryItem.Category;
import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.item.dto.DataItem;
import br.com.rosa.domain.item.dto.RegisterItem;
import br.com.rosa.domain.item.dto.UpdateItem;
import br.com.rosa.domain.item.validation.ValidateIfExists;
import br.com.rosa.infra.exceptions.SqlConstraintViolationException;
import br.com.rosa.infra.exceptions.ValidationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Autowired
    @InjectMocks
    private ItemService serviceItemTest;

    @Mock
    private RepositoryItem repositoryItemTest;

    @Mock
    private RepositoryCategory repositoryCategoryTest;

    @Mock
    private ValidateIfExists validateIfExistsTest;

    @Test
    @DisplayName("Should success if item save in repository")
    void createItem01() throws IOException {

        var dtoItem = new RegisterItem(1L, "test", 20.30, 1, "testCategory");

        var mockFile = returnImgFake();

        var category = new Category(1L, "testCategory");

        when(repositoryCategoryTest.getReferenceByName(dtoItem.category())).thenReturn(category);

        // Mockando método estático pois o TransformeAndRisezeImage.saveImgItem é static
        try (var mockedStatic = Mockito.mockStatic(TransformAndResizeImage.class)) {
            mockedStatic.when(() -> TransformAndResizeImage.saveImgItem(mockFile)).thenReturn(mockFile.getBytes());

            var item = new Item(dtoItem, category.getId(), mockFile.getBytes());

            serviceItemTest.createItem(mockFile, dtoItem);

            verify(repositoryItemTest, times(1)).save(item);
        }
    }

    @Test
    @DisplayName("Should throw ValidationException if format img invalid")
    void createItem02() throws IOException {

        var dtoItem = new RegisterItem(1L, "test", 20.30, 1, "testCategory");

        // Criação de um arquivo de imagem fake
        MockMultipartFile mockFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{1, 2, 3, 4});

        var category = new Category(1L, "testCategory");

        when(repositoryCategoryTest.getReferenceByName(dtoItem.category())).thenReturn(category);

        ValidationException exception =  Assertions.assertThrows(ValidationException.class, () -> {
            serviceItemTest.createItem(mockFile, dtoItem);
        });

        Assertions.assertEquals("Formatação de imagem errada", exception.getMessage());

    }

    @Test
    @DisplayName("Should return SqlConstraintViolationException")
    void createItem03() throws IOException {

        var dtoItem = new RegisterItem(1L, "test", 20.30, 1, "testCategory");

        doThrow(new SqlConstraintViolationException("Código do produto já existe")).when(
                validateIfExistsTest).validateRegisterItem(dtoItem.cod(), dtoItem.category(), dtoItem.name());

        SqlConstraintViolationException exception =  Assertions.assertThrows(SqlConstraintViolationException.class, () -> {
            validateIfExistsTest.validateRegisterItem(dtoItem.cod(), dtoItem.category(), dtoItem.name());
        });

        Assertions.assertEquals("Código do produto já existe", exception.getMessage());

    }

    @Test
    @DisplayName("Should return a list page")
    void forListItems() throws IOException {

        List<Item> items = new ArrayList<>();
        var mockFile = returnImgFake();
        items.add(new Item(new RegisterItem(1L, "test", 20.30, 1, "testCategory"),
                1L, mockFile.getBytes()));

        List<DataItem> listItems = new ArrayList<>();
        items.forEach(item -> {

            //Pega a imagem da pasta com a url salva no banco de dados
            var base64Image = TransformAndResizeImage.takeImage(item.getImg());

            //Criamos a instancia do DTO(Record) e adicionamos na lista
            DataItem i = new DataItem(item, base64Image);
            listItems.add(i);
        });


        Pageable page = PageRequest.of(0, 5);
        int start = Math.min((int)page.getOffset(), listItems.size());
        int end = Math.min((start + page.getPageSize()), listItems.size());
        Page<DataItem> pagina = new PageImpl<DataItem>(listItems.subList(start, end), page, listItems.size());

        var returnList = serviceItemTest.forListItems(items, page);

        assertThat(returnList).isEqualTo(pagina);

    }

    @Test
    @DisplayName("Should return updated item")
    void updateItem01() throws IOException {

        var updateItem = new UpdateItem(1L, 1L, "test", 10, 20, 1L, "1L");

        var mockFile = returnImgFake();

        var dtoItem = new RegisterItem(1L, "test", 20.30, 1, "testCategory");

        var item = new Item(dtoItem, 1L, mockFile.getBytes());

        when(repositoryItemTest.getReferenceById(updateItem.id())).thenReturn(item);

        var itemService = serviceItemTest.updateItem(updateItem, mockFile);

        verify(validateIfExistsTest,times(1)).validateUpdateItem(updateItem, item);

        verify(repositoryItemTest, times(1)).save(item);

        assertThat(itemService).isEqualTo(item);
    }

    @Test
    @DisplayName("Should return SqlConstraintViolationException")
    void updateItem02() throws IOException {

        var updateItem = new UpdateItem(1L, 1L, "test", 10, 20, 1L, "1L");

        var mockFile = returnImgFake();

        var dtoItem = new RegisterItem(1L, "test", 20.30, 1, "testCategory");

        var item = new Item(dtoItem, 1L, mockFile.getBytes());

        when(repositoryItemTest.getReferenceById(updateItem.id())).thenReturn(item);

        doThrow(new SqlConstraintViolationException("Código do produto já existe")).when(validateIfExistsTest).validateUpdateItem(updateItem, item);

        SqlConstraintViolationException exception = Assertions.assertThrows(SqlConstraintViolationException.class, () -> {
            serviceItemTest.updateItem(updateItem, mockFile);
        });

        Assertions.assertEquals("Código do produto já existe", exception.getMessage());
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