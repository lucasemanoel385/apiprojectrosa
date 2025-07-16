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

    @PostMapping
    public ResponseEntity<Page<ItemsAvailableDTO>> getListItems(@PageableDefault(sort = "start_date", direction = Sort.Direction.ASC, size = 5) Pageable page,
                                                                   @RequestBody CheckItemsDTO data) {

        var listaItems = data.dateStart() == null ?
                repositoryItemContract.findFilteredItensWithContractIdNotDate(page,
                        data.search(), LocalDate.now(), String.valueOf(SituationContract.RESERVADO)).map(item -> {
                            var check = checkItemIfAvaible.validate(item, data);
                            Item itemRepo = repositoryItem.getReferenceByCod(item.getCod());

                            return new ItemsAvailableDTO(item, check, TransformAndResizeImage.takeImage(itemRepo.getImg()));
                        })
                :
                repositoryItemContract.findAllItemsReservedInDateWithContract(page,
                data.search(), data.dateStart(), data.dateFinal(), String.valueOf(SituationContract.RESERVADO)).map(item -> {
                    var check = checkItemIfAvaible.validate(item, data);
                    Item itemRepo = repositoryItem.getReferenceByCod(item.getCod());

                    return new ItemsAvailableDTO(item, check, TransformAndResizeImage.takeImage(itemRepo.getImg()));
                });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return ResponseEntity.ok().headers(headers).body(listaItems);
    }


}
