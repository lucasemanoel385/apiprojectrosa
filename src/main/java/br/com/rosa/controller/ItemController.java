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
import br.com.rosa.domain.item.dto.AtualizarItem;
import br.com.rosa.domain.item.dto.DadosItem;
import br.com.rosa.domain.item.dto.ItemCadastro;
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
	public ResponseEntity<DadosItem> registerItem(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart(name = "item") ItemCadastro dados, UriComponentsBuilder uriBuilder) {
		
		var item = service.createItem(file, dados);

		var uri = uriBuilder.path("/item/{id}").buildAndExpand(item.getId()).toUri(); 
		
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping
	public ResponseEntity<Page<DadosItem>> getItens(
			@PageableDefault(page = 0 ,sort = "name",size = 5, direction = Direction.ASC) Pageable page,
			@RequestParam(required = false) String search) {

		var listItens = service.listItens(page, search);
		
		HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		
		return ResponseEntity.ok().headers(headers).body(listItens);
		
	}

	@GetMapping("all")
	public ResponseEntity<Page<DadosItem>> getAllItens(@PageableDefault(sort = "name", size = 10000, direction = Direction.DESC) Pageable page) {

		var listItens = service.forListItens(repository.findAll() ,page);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(listItens);

	}

	@GetMapping("{id}")
	public ResponseEntity<DadosItem> getItens(@PathVariable Long id) {

		var item = service.getItemId(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		return ResponseEntity.ok().headers(headers).body(item);

	}
	
	@PatchMapping
	@Transactional
	public ResponseEntity<DadosItem> updateItem(@RequestPart(name = "file", required = false) MultipartFile file, @RequestPart(name = "item") AtualizarItem dados) {

		var item = service.updateItem(dados, file);
		
		return ResponseEntity.ok().body(new DadosItem(item, TransformeAndResizeImage.takeImage(item.getImg())));
		
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity<Page<DadosItem>> deleteItem(@PathVariable Long id) {
		repository.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}

}
