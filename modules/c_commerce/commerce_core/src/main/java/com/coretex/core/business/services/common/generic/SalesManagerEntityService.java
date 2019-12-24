package com.coretex.core.business.services.common.generic;

import com.coretex.commerce.core.services.PageableService;
import com.coretex.items.core.GenericItem;

import java.util.List;
import java.util.UUID;

public interface SalesManagerEntityService<E extends GenericItem> extends PageableService<E> {

	void save(E entity);
	void refresh(E entity);

	@Deprecated
	void update(E entity);

	void create(E entity);

	void delete(E entity);

	E getByUUID(UUID id);

	List<E> list();

	Long count();

}
