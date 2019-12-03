package com.coretex.core.business.services.common.generic;

import java.util.List;
import java.util.UUID;

public interface SalesManagerEntityService<E> extends PageableEntityService<E> {

	void save(E entity);

	@Deprecated
	void update(E entity);

	void create(E entity);

	void delete(E entity);

	E getByUUID(UUID id);

	List<E> list();

	Long count();

}
