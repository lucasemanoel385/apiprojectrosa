package br.com.rosa.infra.exceptions;

public class AutenticationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public AutenticationException(String message) {
		super(message);
	}

}
