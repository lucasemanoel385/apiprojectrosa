package br.com.rosa.controller;

import br.com.rosa.domain.categoryItem.dto.UpdateCategory;
import br.com.rosa.domain.categoryItem.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rosa.domain.categoryItem.RepositoryCategoria;
import br.com.rosa.domain.categoryItem.dto.RegisterCategory;
import br.com.rosa.domain.categoryItem.dto.DadosCategoria;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("categoria")
public class CategoriaController {
	
	@Autowired
	private RepositoryCategoria repository;

	@Autowired
	private CategoryService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DadosCategoria> registerCategory(@RequestBody RegisterCategory data, UriComponentsBuilder uriBuilder) {
		
		var categoria = service.registerCategory(data);
		
		var uri = uriBuilder.path("/categoria/{id}").buildAndExpand(categoria.getId()).toUri(); 
		
		return ResponseEntity.created(uri).body(new DadosCategoria(categoria));
		
	}
	
	@GetMapping
	public ResponseEntity<Page<DadosCategoria>> getAllCategory(
			@PageableDefault(page = 0, size = 5, sort = "name" ,direction = Sort.Direction.ASC) Pageable page,
			@RequestParam(required = false) String search) {

		if (search == null) {

			return ResponseEntity.ok(repository.findAll(page).map(DadosCategoria::new));

		} else {

			return ResponseEntity.ok(repository.findAllByName(search, page).map(DadosCategoria::new));

		}
		
	}

	@GetMapping("{id}")
	public ResponseEntity<DadosCategoria> getCategoryId(@PathVariable Long id) {

		var category = repository.getReferenceById(id);

		return ResponseEntity.ok(new DadosCategoria(category));

	}

	@PatchMapping
	@Transactional
	public ResponseEntity updateCategory(@RequestBody UpdateCategory data) {

		var category = service.updateCategory(data);

		return ResponseEntity.ok(new DadosCategoria(category));
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity deleteCategory(@PathVariable Long id) {

		service.deleteCategory(id);
		
		return ResponseEntity.noContent().build();
		
	}

}
