package br.com.rosa.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.UnexpectedTypeException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
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
		return ResponseEntity.badRequest().body(erros.get(0).getDefaultMessage());
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
	
	//Criamos essa DTO pq só vai ser usada aqui
	/*private record DadosErrosValidacaoCadastroVideo(String error) {
		public DadosErrosValidacaoCadastroVideo(FieldError erro) {
			this(erro.getDefaultMessage());
		}
	}*/
	
}
