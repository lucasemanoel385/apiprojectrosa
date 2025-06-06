package br.com.rosa.controller;

import java.time.LocalDate;
import java.util.List;

import br.com.rosa.domain.scheduling.RepositoryScheduling;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rosa.domain.scheduling.Scheduling;
import br.com.rosa.domain.scheduling.dto.SchedulingRegister;
import br.com.rosa.domain.scheduling.service.SchedulingService;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("scheduling")
public class SchedulingController {
	
	@Autowired
	private SchedulingService service;

	@Autowired
	private RepositoryScheduling repositoryScheduling;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@PostMapping
	@Transactional
	public ResponseEntity registerScheduling(@RequestBody @Valid SchedulingRegister data, UriComponentsBuilder uriBuilder) throws Exception {
		
		var scheduling = service.registerScheduling(data);

		var uri = uriBuilder.path("/scheduling/{id}").buildAndExpand(scheduling.getId()).toUri();

		messagingTemplate.convertAndSend("/topic/scheduling", repositoryScheduling.findAllByDateScheduling(data.dateScheduling().toString()));

		return ResponseEntity.created(uri).build();
	}

	
	@GetMapping("{dateDay}")
	public ResponseEntity<List<Scheduling>> getSchedulings(@PathVariable String dateDay) {

		var listScheduling = service.listDateScheduling(dateDay);

		return ResponseEntity.ok(listScheduling);
		
	}
	
	@DeleteMapping("{id}/{dateDay}")
	@Transactional
	public ResponseEntity deleteScheduling(@PathVariable Long id, @PathVariable String dateDay) {
		
		service.deleteScheduling(id);
		messagingTemplate.convertAndSend("/topic/scheduling", repositoryScheduling.findAllByDateScheduling(dateDay));
		return ResponseEntity.noContent().build();
	}
}
