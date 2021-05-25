package com.coretex.core.activeorm.services.impl;

import com.coretex.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.core.activeorm.services.ItemService;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.core.services.items.context.factory.ItemContextFactory;
import com.coretex.core.services.items.exceptions.ItemCreationException;
import com.coretex.items.core.GenericItem;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.meta.AbstractGenericItem;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.UUID;

import static com.coretex.core.services.bootstrap.BootstrapConfig.ITEM_CONTEXT_FACTORY_QUALIFIER;
import static com.coretex.core.services.bootstrap.BootstrapConfig.META_TYPE_PROVIDER_QUALIFIER;
import static org.springframework.util.Assert.notNull;

public class DefaultItemService implements ItemService {

	@Autowired
	@Qualifier(ITEM_CONTEXT_FACTORY_QUALIFIER)
	private ItemContextFactory itemContextFactory;

	@Autowired
	@Qualifier(META_TYPE_PROVIDER_QUALIFIER)
	private MetaTypeProvider metaTypeProvider;

	@Autowired
	private ActiveOrmOperationExecutor activeOrmOperationExecutor;

	@Override
	public <T extends AbstractGenericItem>  T create(Class<T> itemClass){
		try {
			return ConstructorUtils.invokeConstructor(itemClass, itemContextFactory.create(itemClass));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new ItemCreationException("Can't create item", e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T extends AbstractGenericItem> T create(String typeCode){
		try {
			MetaTypeItem metaTypeItem = metaTypeProvider.findMetaType(typeCode);
			notNull(metaTypeItem, String.format("Cant find type with type code: %s", typeCode));
			return ConstructorUtils.invokeConstructor((Class<T>) metaTypeItem.getItemClass(), itemContextFactory.create(typeCode));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new ItemCreationException(String.format("Can't create item: %s", typeCode), e);
		}
	}

	@Override
	public <T extends AbstractGenericItem> T create(Class<T> itemClass, UUID uuid){
		try {
			return ConstructorUtils.invokeConstructor(itemClass, itemContextFactory.create(itemClass, uuid));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
			throw new ItemCreationException("Can't create item", e);
		}
	}

	@Override
	public <T extends GenericItem> T save(T item){
		activeOrmOperationExecutor.executeSaveOperation(item);
		return item;
	}


	@Override
	public <T extends GenericItem> void saveAll(Collection<T> items){
		items.forEach(this::save);
	}

	@Override
	public <T extends GenericItem> void delete(T item){
		item.getItemContext().refresh();
		activeOrmOperationExecutor.executeDeleteOperation(item);
	}

	@Override
	public <T extends GenericItem> void deleteAll(Collection<T> items){
		items.forEach(this::delete);
	}
}
