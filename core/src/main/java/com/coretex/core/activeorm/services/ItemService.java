package com.coretex.core.activeorm.services;

import com.coretex.items.core.GenericItem;
import com.coretex.meta.AbstractGenericItem;

import java.util.Collection;
import java.util.UUID;

public interface ItemService {


	<T extends AbstractGenericItem> T create(Class<T> itemClass);

	@SuppressWarnings("unchecked")
	<T extends AbstractGenericItem> T create(String typeCode);

	<T extends AbstractGenericItem> T create(Class<T> itemClass, UUID uuid);

	<T extends GenericItem> T save(T item);

	<T extends GenericItem>  void saveAll(Collection<T> items);

	<T extends GenericItem>  void delete(T item);

	<T extends GenericItem>  void deleteAll(Collection<T> items);
}
