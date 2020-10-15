package com.coretex.core.activeorm.services.impl;

import com.coretex.core.activeorm.interceptors.Interceptor;
import com.coretex.core.activeorm.interceptors.OnCreateInterceptor;
import com.coretex.core.activeorm.interceptors.OnLoadInterceptor;
import com.coretex.core.activeorm.interceptors.OnRemoveInterceptor;
import com.coretex.core.activeorm.interceptors.OnSavePrepareInterceptor;
import com.coretex.core.activeorm.interceptors.OnSavedInterceptor;
import com.coretex.core.activeorm.services.ItemInterceptorsRegistry;
import com.coretex.core.general.utils.ItemUtils;
import com.coretex.core.services.bootstrap.meta.MetaTypeProvider;
import com.coretex.core.utils.TypeUtil;
import com.coretex.items.core.MetaTypeItem;
import com.coretex.server.ApplicationConfig;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.EMPTY_LIST;

public class DefaultItemInterceptorsRegistry implements ItemInterceptorsRegistry {

	@Resource
	private ApplicationConfig applicationConfig;
	@Resource
	private MetaTypeProvider metaTypeProvider;
	private ApplicationContext applicationContext;

	private Collection<Object> interceptors;
	private final Map<String, MappedInterceptors> typeToInterceptorsMap = new ConcurrentHashMap<>(256, 0.75F, 32);

	private static final Logger LOG = LoggerFactory.getLogger(DefaultItemInterceptorsRegistry.class);

	public final String SYSTEM_ORM_INTERCEPTORS_ALL_ACTIVE = "system.orm.interceptors.active";
	public final String SYSTEM_ORM_INTERCEPTORS_ACTIVE = "system.orm.interceptors.%s.active";
	public final String SYSTEM_ORM_INTERCEPTORS_ITEM_ACTIVE = "system.orm.interceptors.item.%s.active";
	public final String SYSTEM_ORM_INTERCEPTORS_TYPE_ACTIVE = "system.orm.interceptors.action.type.%s.active";


	@PostConstruct
	private void init() {

		var allInterceptorActive = this.applicationConfig.getConfig(SYSTEM_ORM_INTERCEPTORS_ALL_ACTIVE, Boolean.TYPE)
				.orElse(Boolean.TRUE);

		if(!allInterceptorActive){
			LOG.info("Interceptors deactivated");
		}else {
			this.interceptors = applicationContext.getBeansWithAnnotation(Interceptor.class).values();
			LOG.info("Loading interceptors...");
			if (CollectionUtils.isNotEmpty(interceptors)) {
				interceptors.forEach(this::initInterceptor);
			}
		}

	}

	private void initInterceptor(Object o) {
		var annotation = AnnotationUtils.findAnnotation(o.getClass(), Interceptor.class);
		var items = annotation.items();

		Arrays.stream(items)
				.map(item -> metaTypeProvider.findMetaType(TypeUtil.getMetaTypeCode(item)))
				.forEach(item -> registerInterceptor(item, o));

	}

	private void registerInterceptor(MetaTypeItem item, Object o) {
		if (isActive(item, o)) {
			typeToInterceptorsMap.computeIfAbsent(item.getTypeCode(), s -> new MappedInterceptors(o));
			typeToInterceptorsMap.computeIfPresent(item.getTypeCode(), (s, mappedInterceptors) -> {
				mappedInterceptors.addInterceptor(o);
				return mappedInterceptors;
			});
			if (CollectionUtils.isNotEmpty(item.getSubtypes())) {
				ItemUtils.getAllSubtypes(item).forEach(sub -> registerInterceptor(sub, o));
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Register new interceptor [%s] for item::%s", o.getClass().getName(), item.getTypeCode()));
			}
		} else {
			if (LOG.isDebugEnabled()) {
				LOG.debug(String.format("Interceptor [%s] for item::%s deactivated", o.getClass().getName(), item.getTypeCode()));
			}
		}
	}

	private boolean isActive(MetaTypeItem item, Object o) {
		var itemInterceptorActive = this.applicationConfig.getConfig(String.format(SYSTEM_ORM_INTERCEPTORS_ITEM_ACTIVE, item.getTypeCode()), Boolean.TYPE)
				.orElse(Boolean.TRUE);
		var interceptorActive = this.applicationConfig.getConfig(String.format(SYSTEM_ORM_INTERCEPTORS_ACTIVE, o.getClass().getSimpleName()), Boolean.TYPE)
				.orElse(Boolean.TRUE);
		return interceptorActive && itemInterceptorActive;
	}

	@Override
	public Collection<OnCreateInterceptor> getCreateInterceptors(String type) {
		return getInterceptors(type, MappedInterceptorType.CREATE);
	}

	@Override
	public Collection<OnLoadInterceptor> getLoadInterceptors(String type) {
		return getInterceptors(type, MappedInterceptorType.LOAD);
	}

	@Override
	public Collection<OnRemoveInterceptor> getRemoveInterceptors(String type) {
		return getInterceptors(type, MappedInterceptorType.REMOVE);
	}

	@Override
	public Collection<OnSavePrepareInterceptor> getSavePrepareInterceptors(String type) {
		return getInterceptors(type, MappedInterceptorType.SAVE_PREPARE);
	}

	@Override
	public Collection<OnSavedInterceptor> getSavedInterceptors(String type) {
		return getInterceptors(type, MappedInterceptorType.SAVED);
	}


	<I> Collection<I> getInterceptors(String type, MappedInterceptorType interceptorType) {
		if (typeToInterceptorsMap.containsKey(type)) {
			return typeToInterceptorsMap.get(type).getInterceptors(interceptorType);
		} else {
			return EMPTY_LIST;
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private class MappedInterceptors {

		private final Collection<OnCreateInterceptor> createInterceptorMapping = Sets.newLinkedHashSet();
		private final Collection<OnLoadInterceptor> loadInterceptorMapping = Sets.newLinkedHashSet();
		private final Collection<OnRemoveInterceptor> removeInterceptorMapping = Sets.newLinkedHashSet();
		private final Collection<OnSavePrepareInterceptor> savePrepareInterceptorMapping = Sets.newLinkedHashSet();
		private final Collection<OnSavedInterceptor> savedInterceptorMapping = Sets.newLinkedHashSet();

		private final Map<MappedInterceptorType, Collection> mappedInterceptorTypeCollectionMap = Maps.newHashMap();

		{
			mappedInterceptorTypeCollectionMap.put(MappedInterceptorType.CREATE, createInterceptorMapping);
			mappedInterceptorTypeCollectionMap.put(MappedInterceptorType.LOAD, loadInterceptorMapping);
			mappedInterceptorTypeCollectionMap.put(MappedInterceptorType.REMOVE, removeInterceptorMapping);
			mappedInterceptorTypeCollectionMap.put(MappedInterceptorType.SAVE_PREPARE, savePrepareInterceptorMapping);
			mappedInterceptorTypeCollectionMap.put(MappedInterceptorType.SAVED, savedInterceptorMapping);
		}


		public MappedInterceptors(Object o) {
			addInterceptor(o);
		}

		public void addInterceptor(Object interceptor) {
			for (MappedInterceptorType value : MappedInterceptorType.values()) {
				if (value.clazz.isInstance(interceptor)) {
					var typeInterceptorActive = applicationConfig.getConfig(String.format(SYSTEM_ORM_INTERCEPTORS_TYPE_ACTIVE, value.toString()), Boolean.TYPE)
							.orElse(Boolean.TRUE);
					if (typeInterceptorActive) {
						getMappedInterceptorTypeCollectionMap().get(value).add(interceptor);
					}else {
						LOG.info(String.format("Interceptor action type::[%s] is deactivated", value.toString()));
					}
				}
			}
		}

		public <I> Collection<I> getInterceptors(MappedInterceptorType interceptorType) {
			return interceptorType.getInterceptors(this);
		}

		protected Map<MappedInterceptorType, Collection> getMappedInterceptorTypeCollectionMap() {
			return mappedInterceptorTypeCollectionMap;
		}
	}

	private enum MappedInterceptorType {
		CREATE(OnCreateInterceptor.class),
		LOAD(OnLoadInterceptor.class),
		REMOVE(OnRemoveInterceptor.class),
		SAVED(OnSavedInterceptor.class),
		SAVE_PREPARE(OnSavePrepareInterceptor.class);

		private final Class clazz;

		MappedInterceptorType(Class clazz) {
			this.clazz = clazz;
		}

		@SuppressWarnings("unchecked")
		private <I> Collection<I> getInterceptors(MappedInterceptors mappedInterceptors) {
			return (Collection<I>) mappedInterceptors.getMappedInterceptorTypeCollectionMap().getOrDefault(this, EMPTY_LIST);
		}

	}


}
