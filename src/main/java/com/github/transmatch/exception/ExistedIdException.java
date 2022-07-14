package com.github.transmatch.exception;

public class ExistedIdException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ExistedIdException() {
		super();
	}

	public ExistedIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ExistedIdException(String message, Throwable cause) {
		super(message, cause);
	}

	public ExistedIdException(String message) {
		super(message);
	}

	public ExistedIdException(Throwable cause) {
		super(cause);
	}

}
