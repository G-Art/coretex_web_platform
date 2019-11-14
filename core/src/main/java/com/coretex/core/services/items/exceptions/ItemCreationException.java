package com.coretex.core.services.items.exceptions;

import com.coretex.core.services.exceptions.CoreServiceException;

public class ItemCreationException extends CoreServiceException {

	public ItemCreationException(String message) {
		super(message);
	}

	public ItemCreationException(String message, Throwable cause) {
		super(message, cause);
	}
}
