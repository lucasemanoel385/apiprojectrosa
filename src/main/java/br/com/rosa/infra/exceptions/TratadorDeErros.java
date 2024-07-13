package br.com.rosa.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

//RestControllerAdvice fala pro spring que é essa classe que vai tratar os erros da API
@RestControllerAdvice
public class TratadorDeErros {

	//Error 404 é tratado quando o notfoundexception que é lançada pelo jpa
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity tratarErro404() {

		return ResponseEntity.notFound().build();
	}
	
	//Erro pra quando tem algum campo invalido
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity tratarErro400(MethodArgumentNotValidException ex) {
		var erros = ex.getFieldErrors();

												//erros me da uma stream e mape-e cada objeto field erro para um objeto erro validacao e me devolvendo uma lista
		return ResponseEntity.badRequest().body(erros.stream().map(DataErrorNotNull::new).toList());
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity ErrorNullColumnBDD(SQLIntegrityConstraintViolationException ex) {
		return ResponseEntity.status(400).body(ex.getMessage());
	}
	
	@ExceptionHandler(ValidacaoException.class)
	public ResponseEntity tratarErroRegraDeNegocio(ValidacaoException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(SqlConstraintViolationException.class)
	public ResponseEntity tratarErroRegraDeNegocio(SqlConstraintViolationException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity fieldNull(NullPointerException ex) {
		return ResponseEntity.badRequest().body(ex.getMessage());
	}

	@ExceptionHandler(AutenticationException.class)
	public ResponseEntity tratarErroAutenticacao(AutenticationException ex) {
		return ResponseEntity.status(403).body(ex.getMessage());
	}

	@ExceptionHandler(UnexpectedTypeException.class)
	public ResponseEntity tratarErroAutenticacao(UnexpectedTypeException ex) {
		return ResponseEntity.status(400).body(ex.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity tratarErroAutenticacao(HttpMessageNotReadableException ex) {
		return ResponseEntity.status(400).body(ex.getMessage());
	}


	//Criamos essa DTO pq só vai ser usada aqui
	public record DataErrorNotNull(String field, String message) {
		public DataErrorNotNull(FieldError error) {

			this(error.getField(), error.getDefaultMessage());
		}
	}
	
}
