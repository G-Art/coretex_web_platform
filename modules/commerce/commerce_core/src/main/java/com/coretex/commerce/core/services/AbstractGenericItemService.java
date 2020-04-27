package com.coretex.commerce.core.services;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.core.GenericItem;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class AbstractGenericItemService<E extends GenericItem> implements GenericItemService<E> {

	private final Class<E> objectClass;

	private final Dao<E> repository;

	@SuppressWarnings("unchecked")
	public AbstractGenericItemService(final Dao<E> repository) {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.objectClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
		this.repository = repository;
	}

	protected final Class<E> getObjectClass() {
		return objectClass;
	}


	public E getByUUID(UUID uuid) {
		return repository.findSingle(Map.of(GenericItem.UUID, uuid), true);
	}


	public void save(E item) {
		repository.save(item);
	}

	public void refresh(E item) {
		item.getItemContext().refresh();
	}

	public void create(E item) {
		save(item);
	}

	@Deprecated
	public void update(E item) {
		save(item);
	}


	public void delete(E item) {
		repository.delete(item);
	}


	public List<E> list() {
		return repository.find();
	}

	public Stream<E> listReactive() {
		return repository.findReactive();
	}

	public Long count() {
		return repository.count();
	}

	@Override
	public PageableSearchResult<E> pageableList() {
		return repository.findPageable();
	}

	@Override
	public PageableSearchResult<E> pageableList(long count) {
		return repository.findPageable(count);
	}

	@Override
	public PageableSearchResult<E> pageableList(long count, long page) {
		return repository.findPageable(count, page);
	}

	public Dao<E> getRepository() {
		return repository;
	}
}
