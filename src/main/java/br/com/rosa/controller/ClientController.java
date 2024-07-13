package br.com.rosa.controller;

import br.com.rosa.domain.client.Client;
import br.com.rosa.domain.client.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rosa.domain.client.RepositoryCliente;
import br.com.rosa.domain.client.dto.UpdateClient;
import br.com.rosa.domain.client.dto.ClientRegister;
import br.com.rosa.domain.client.dto.DataClient;
import br.com.rosa.domain.item.dto.DataItem;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("cliente")
public class ClientController {
	
	@Autowired
	private RepositoryCliente repository;

	@Autowired
	private ClientService service;
	
	@PostMapping
	@Transactional
	public ResponseEntity<DataClient> registerClient(@RequestBody @Valid ClientRegister dados, UriComponentsBuilder uriBuilder) {

		var client = service.registerClient(dados);

		var uri = uriBuilder.path("/cliente/{id}").buildAndExpand(client.getId()).toUri();
		
		return ResponseEntity.created(uri).body(new DataClient(client));
		
	}
	
	@GetMapping
	public ResponseEntity<Page<DataClient>> getClients(
			@PageableDefault(page = 0, size = 5, sort = "name_reason" ,direction = Sort.Direction.ASC) Pageable page,
			@RequestParam(required = false) String search) {

		if (search == null ) {
			return ResponseEntity.ok(repository.findAll(page).map(DataClient::new));
		} else {
			var clientsFilter = service.filterClients(search, page);

			return ResponseEntity.ok(clientsFilter.map(DataClient::new));
		}

		
	}

	@GetMapping("filter")
	public ResponseEntity<Page<DataClient>> getClientsFilter(
			@PageableDefault(size = 10000, sort = "name_reason" ,direction = Sort.Direction.ASC) Pageable page,
			@RequestParam(required = false) String search) {
		System.out.println(search);
		var clientsFilter = service.filterClients(search, page);

		return ResponseEntity.ok(clientsFilter.map(DataClient::new));

	}

	@GetMapping("{id}")
	public ResponseEntity<Client> getClientsId(@PathVariable Long id) {
		var client = service.getClientId(id);
		return ResponseEntity.ok(client);

	}
	
	@PatchMapping
	@Transactional
	public ResponseEntity<DataClient> updateCliente(@Valid @RequestBody UpdateClient dados) {
		
		var client = service.updateClient(dados);

		return ResponseEntity.ok(new DataClient(client));
		
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity<Page<DataItem>> deleteClient(@PathVariable Long id) {
		service.deleteId(id);
		return ResponseEntity.noContent().build();
	}

}
