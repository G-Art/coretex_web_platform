package com.coretex.core.business.services.common.generic;

import java.util.List;
import java.util.UUID;


public interface SalesManagerEntityService<E> extends TransactionalAspectAwareService {

	/**
	 * Crée l'entité dans la base de données. Mis à part dans les tests pour faire des sauvegardes simples, utiliser
	 * create() car il est possible qu'il y ait des listeners sur la création d'une entité.
	 *
	 * @param entity entité
	 */
	void save(E entity);

	/**
	 * Met à jour l'entité dans la base de données.
	 *
	 * @param entity entité
	 */
	void update(E entity);

	/**
	 * Crée l'entité dans la base de données.
	 *
	 * @param entity entité
	 */
	void create(E entity);

	/**
	 * Supprime l'entité de la base de données
	 *
	 * @param entity entité
	 */
	void delete(E entity);


	/**
	 * Retourne une entité à partir de son id.
	 *
	 * @param id identifiant
	 * @return entité
	 */
	E getById(UUID id);

	/**
	 * Renvoie la liste de l'ensemble des entités de ce type.
	 *
	 * @return liste d'entités
	 */
	List<E> list();


	/**
	 * Compte le nombre d'entités de ce type présentes dans la base.
	 *
	 * @return nombre d'entités
	 */
	Long count();

}
