package com.coretex.core.activeorm.exceptions;

public class QueryException extends RuntimeException {

	public QueryException() {
		super();
	}

	public QueryException(String message) {
		super(message);
	}

	public QueryException(String message, Throwable cause) {
		super(message, cause);
	}

	public QueryException(Throwable cause) {
		super(cause);
	}

	protected QueryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
