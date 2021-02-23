package com.coretex.commerce.core.services;

import com.coretex.items.core.GenericItem;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.UUID;

public interface GenericItemService<E extends GenericItem> extends PageableService<E> {

	E getByUUID(UUID uuid);

	void save(E item);

	void refresh(E item);

	void create(E item);

	void delete(E item);

	List<E> list();
	Flux<E> listReactive();

	Long count();
}
