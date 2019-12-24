package com.coretex.commerce.core.services;

import com.coretex.items.core.GenericItem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface GenericItemService<E extends GenericItem> extends PageableService<E> {

	E getByUUID(UUID id);

	void save(E entity);

	void refresh(E entity);

	void create(E entity);

	void delete(E entity);

	List<E> list();
	Stream<E> listReactive();

	Long count();
}
