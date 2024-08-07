package br.com.rosa.controller;

import java.util.List;

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

import br.com.rosa.domain.task.RepositoryTask;
import br.com.rosa.domain.task.Task;
import br.com.rosa.domain.task.dto.TaskRegister;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("task")
public class TasksController {
	
	@Autowired
	private RepositoryTask taskRepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@PostMapping
	@Transactional
	public ResponseEntity registerTask(@RequestBody @Valid TaskRegister data, UriComponentsBuilder uriBuilder) {
		
		var task = new Task(data);
		
		taskRepository.save(task);

		var uri = uriBuilder.path("/task/{id}").buildAndExpand(task.getId()).toUri();

		messagingTemplate.convertAndSend("/topic/task", taskRepository.findAll());

		return ResponseEntity.created(uri).build();
		
	}
	
	@GetMapping
	public ResponseEntity<List<Task>> getTasks() {
		
		return ResponseEntity.ok(taskRepository.findAll());
	}
	
	@DeleteMapping("{id}")
	@Transactional
	public ResponseEntity deleteTask(@PathVariable Long id) {
		
		taskRepository.deleteById(id);

		messagingTemplate.convertAndSend("/topic/task", taskRepository.findAll());

		return ResponseEntity.noContent().build();
		
	}

}
