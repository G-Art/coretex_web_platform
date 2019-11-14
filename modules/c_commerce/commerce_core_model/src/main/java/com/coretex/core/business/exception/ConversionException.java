
package com.coretex.core.business.exception;


public class ConversionException extends RuntimeException {
	private static final long serialVersionUID = 687400310032876603L;

	public ConversionException(final String msg, final Throwable cause) {
		super(msg, cause);
	}

	public ConversionException(final String msg) {
		super(msg);
	}

	public ConversionException(Throwable t) {
		super(t);
	}


}
