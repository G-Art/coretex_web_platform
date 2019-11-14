package com.coretex.core.business.services.common.generic;

import com.coretex.core.activeorm.dao.Dao;
import com.coretex.meta.AbstractGenericItem;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class SalesManagerEntityServiceImpl<E extends AbstractGenericItem>
		implements SalesManagerEntityService<E> {

	/**
	 * Classe de l'entité, déterminé à partir des paramètres generics.
	 */
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


	public E getById(UUID id) {
		return repository.findSingle(Map.of("uuid", id), true);
	}


	public void save(E entity) {
		repository.save(entity);
	}


	public void create(E entity) {
		save(entity);
	}


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

}