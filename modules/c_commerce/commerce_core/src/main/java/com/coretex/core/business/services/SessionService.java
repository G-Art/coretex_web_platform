package com.coretex.core.business.services;

import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.servlet.http.HttpSession;

public interface SessionService {

	HttpSession getCurrentSession();

	boolean hasCurrentSession();

	@Nullable
	Object getSessionAttribute(String attribute);

	@Nullable
	<T> T getSessionAttribute(String attribute, Class<T> expectedClass);

	void setSessionAttribute(String key, Object value);

	void removeSessionAttribute(String key);
}
