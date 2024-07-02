package br.com.rosa.infra.exceptions;

public class NullPointerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NullPointerException(String mensagem) {
		super(mensagem);
	}

}
