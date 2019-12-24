package com.coretex.core.business.services.common.generic;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.core.activeorm.services.PageableSearchResult;
import com.coretex.items.core.GenericItem;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class SalesManagerEntityServiceImpl<E extends GenericItem>
		implements SalesManagerEntityService<E> {

	private Class<E> objectClass;

	private Dao<E> repository;

	@SuppressWarnings("unchecked")
	public SalesManagerEntityServiceImpl(Dao<E> repository) {
		ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
		this.objectClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
		this.repository = repository;
	}

	protected final Class<E> getObjectClass() {
		return objectClass;
	}


	public E getByUUID(UUID id) {
		return repository.findSingle(Map.of(GenericItem.UUID, id), true);
	}


	public void save(E entity) {
		repository.save(entity);
	}

	@Override
	public void refresh(E entity) {
		entity.getItemContext().refresh();
	}

	public void create(E entity) {
		save(entity);
	}

	@Deprecated
	public void update(E entity) {
		save(entity);
	}


	public void delete(E entity) {
		repository.delete(entity);
	}


	public List<E> list() {
		return repository.find();
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
}