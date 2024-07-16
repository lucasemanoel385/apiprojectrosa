package br.com.rosa.controller;

import br.com.rosa.domain.TransformeAndResizeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rosa.domain.item.RepositoryItem;
import br.com.rosa.domain.item.dto.UpdateItem;
import br.com.rosa.domain.item.dto.DataItem;
import br.com.rosa.domain.item.dto.RegisterItem;
import br.com.rosa.domain.item.service.ItemService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("item")
public class ItemController {
	
	@Autowired
	private RepositoryItem repository;
	
	@Autowired
	private ItemService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DataItem> registerItem(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart(name = "item") RegisterItem dados, UriComponentsBuilder uriBuilder) {
		
		var item = service.createItem(file, dados);

		var uri = uriBuilder.path("/item/{id}").buildAndExpand(item.getId()).toUri(); 
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping
	public ResponseEntity<Page<DataItem>> getItens(
			@PageableDefault(page = 0 ,sort = "name",size = 5, direction = Direction.ASC) Pageable page,
			@RequestParam(required = false) String search) {

		var listItens = service.listItems(page, search);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
		return ResponseEntity.ok().headers(headers).body(listItens);
		
	}

	@GetMapping("all")
	public ResponseEntity<Page<DataItem>> getAllItens(@PageableDefault(sort = "name", size = 10000, direction = Direction.DESC) Pageable page) {

		var listItens = service.forListItems(repository.findAll() ,page);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(listItens);

	}

	@GetMapping("{id}")
	public ResponseEntity<DataItem> getItens(@PathVariable Long id) {

		var item = service.getItemId(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(item);

	}
	
	@PatchMapping
	@Transactional
	public ResponseEntity<DataItem> updateItem(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart(name = "item") UpdateItem dados) {

		var item = service.updateItem(dados, file);
		
		return ResponseEntity.ok().body(new DataItem(item, TransformeAndResizeImage.takeImage(item.getImg())));
		
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity<Page<DataItem>> deleteItem(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}

}
