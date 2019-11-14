package com.coretex.core.services.items.exceptions;

import com.coretex.core.services.exceptions.CoreServiceException;

public class NotFoundAttributeException extends CoreServiceException {

	public NotFoundAttributeException(String message) {
		super(message);
	}
}
