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

import br.com.rosa.domain.categoryItem.RepositoryCategory;
import br.com.rosa.domain.categoryItem.dto.RegisterCategory;
import br.com.rosa.domain.categoryItem.dto.DataCategory;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("category")
public class CategoryController {
	
	@Autowired
	private RepositoryCategory repository;

	@Autowired
	private CategoryService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DataCategory> registerCategory(@RequestBody RegisterCategory data, UriComponentsBuilder uriBuilder) {
		
		var categoria = service.registerCategory(data);
		
		var uri = uriBuilder.path("/categoria/{id}").buildAndExpand(categoria.getId()).toUri(); 
		
		return ResponseEntity.created(uri).body(new DataCategory(categoria));
		
	}
	
	@GetMapping
	public ResponseEntity<Page<DataCategory>> getAllCategory(
			@PageableDefault(page = 0, size = 5, sort = "name" ,direction = Sort.Direction.ASC) Pageable page,
			@RequestParam(required = false) String search) {

		if (search == null) {

			return ResponseEntity.ok(repository.findAll(page).map(DataCategory::new));

		} else {

			return ResponseEntity.ok(repository.findAllByName(search, page).map(DataCategory::new));

		}
		
	}

	@GetMapping("all")
	public ResponseEntity<Page<DataCategory>> getAllCategory(
			@PageableDefault(page = 0, size = 10000, sort = "name" ,direction = Sort.Direction.ASC) Pageable page) {

		return ResponseEntity.ok(repository.findAll(page).map(DataCategory::new));

	}

	@GetMapping("{id}")
	public ResponseEntity<DataCategory> getCategoryId(@PathVariable Long id) {

		var category = repository.getReferenceById(id);

		return ResponseEntity.ok(new DataCategory(category));

	}

	@PatchMapping
	@Transactional
	public ResponseEntity updateCategory(@RequestBody UpdateCategory data) {

		var category = service.updateCategory(data);

		return ResponseEntity.ok(new DataCategory(category));
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity deleteCategory(@PathVariable Long id) {

		service.deleteCategory(id);
		
		return ResponseEntity.noContent().build();
		
	}

}
