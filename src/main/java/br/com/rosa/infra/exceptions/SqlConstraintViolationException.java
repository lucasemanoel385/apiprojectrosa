package br.com.rosa.infra.exceptions;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class SqlConstraintViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SqlConstraintViolationException(String message) {
		super(message);
	}

}
