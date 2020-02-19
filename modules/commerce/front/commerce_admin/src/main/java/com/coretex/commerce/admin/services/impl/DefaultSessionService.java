package com.coretex.commerce.admin.services.impl;

import com.coretex.commerce.admin.services.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@Service
public class DefaultSessionService implements SessionService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSessionService.class);

	@Override
	public @Nullable HttpSession getCurrentSession() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return request.getSession();
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Not called in the context of an HTTP request");
		}
		return null;
	}

	@Override
	public boolean hasCurrentSession(){
		return Objects.nonNull(getCurrentSession());
	}

	@Override
	public @Nullable Object getSessionAttribute(String attribute){
		HttpSession session = getCurrentSession();
		if (Objects.nonNull(session)) {
			return session.getAttribute(attribute);
		}
		return null;
	}

	@Override
	public @Nullable <T> T getSessionAttribute(String attribute, Class<T> expectedClass){
		Object value = getSessionAttribute(attribute);
		if (Objects.nonNull(value)) {
			return expectedClass.cast(value) ;
		}
		return null;
	}

	@Override
	public void setSessionAttribute(final String key, final Object value){
		HttpSession session = getCurrentSession();
		if (Objects.nonNull(session)) {
			session.setAttribute(key, value);
		}
	}

	@Override
	public void removeSessionAttribute(final String key){
		HttpSession session = getCurrentSession();
		if (Objects.nonNull(session)) {
			session.removeAttribute(key);
		}
	}
}
