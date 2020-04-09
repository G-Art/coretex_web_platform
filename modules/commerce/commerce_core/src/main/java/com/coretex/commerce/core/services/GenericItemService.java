package com.coretex.commerce.core.services;

import com.coretex.items.core.GenericItem;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface GenericItemService<E extends GenericItem> extends PageableService<E> {

	E getByUUID(UUID uuid);

	void save(E item);

	void refresh(E item);

	void create(E item);

	void delete(E item);

	List<E> list();
	Stream<E> listReactive();

	Long count();
}
