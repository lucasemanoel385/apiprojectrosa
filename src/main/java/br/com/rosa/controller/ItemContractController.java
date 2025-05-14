package br.com.rosa.controller;

import br.com.rosa.domain.TransformAndResizeImage;
import br.com.rosa.domain.contract.enunm.SituationContract;
import br.com.rosa.domain.contract.validations.CheckItemIfAvaible;
import br.com.rosa.domain.item.Item;
import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.itemContract.RepositoryItemContract;
import br.com.rosa.domain.itemContract.dto.CheckItemsDTO;
import br.com.rosa.domain.itemContract.dto.ItemsAvailableDTO;
import br.com.rosa.domain.itemContract.dto.ItemsWithContractIdDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("itemContract")
@RestController
public class ItemContractController {

    @Autowired
    private CheckItemIfAvaible checkItemIfAvaible;

    @Autowired
    private RepositoryItemContract repositoryItemContract;

    @Autowired
    private RepositoryItem repositoryItem;


    /*@PostMapping
    public ResponseEntity<Success> verifyItemByDate(@RequestBody @Valid VerifyItemDTO dto) {
        return ResponseEntity.ok(new Success(checkItemIfAvaible.validate(dto)));
    }*/

    @PostMapping
    public ResponseEntity<Page<ItemsAvailableDTO>> getListItems(@PageableDefault(sort = "start_date", direction = Sort.Direction.ASC, size = 5) Pageable page,
                                                                   @RequestBody CheckItemsDTO data) {

        var listItens = forListItems(data.dateStart() == null ?
                repositoryItemContract.findFilteredItensWithContractIdNotDate(
                data.search(), LocalDate.now(), String.valueOf(SituationContract.RESERVADO))
                :
                repositoryItemContract.findAllItemsReservedInDateWithContract(
                data.search(), data.dateStart(), data.dateFinal(), String.valueOf(SituationContract.RESERVADO)),data,page);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(listItens);
    }

    private Page<ItemsAvailableDTO> forListItems(List<ItemsWithContractIdDTO> items, CheckItemsDTO dto, Pageable page) {

        List<ItemsAvailableDTO> listItems = items.stream().map(item -> {
            var check = checkItemIfAvaible.validate(item, dto);

            Item  itemRepo = repositoryItem.getReferenceByCod(item.getCod());

            // Pega a imagem da pasta com a URL salva no banco de dados
            var base64Image = TransformAndResizeImage.takeImage(itemRepo.getImg());

            return new ItemsAvailableDTO(item, check, base64Image);
        }).collect(Collectors.toList());


        if (page.getSort().isSorted()) {
            Sort.Order order = page.getSort().iterator().next(); // Pega só o primeiro campo
            Comparator<ItemsAvailableDTO> comparator = getComparator(order.getProperty());
            if (comparator != null) {
                listItems.sort(order.isAscending() ? comparator : comparator.reversed());
            }
        }

        // Retornar o menor número entre os dois parâmetros
        int start = Math.min((int) page.getOffset(), listItems.size());
        int end = Math.min(start + page.getPageSize(), listItems.size());
        return new PageImpl<ItemsAvailableDTO>(listItems.subList(start, end), page, listItems.size());
    }

    private Comparator<ItemsAvailableDTO> getComparator(String property) {
        return switch (property) {
            case "start_date" -> Comparator.comparing(ItemsAvailableDTO::dateStart);
            default -> null;
        };
    }

    public record Success(
            String message
    ) {

    }

}
