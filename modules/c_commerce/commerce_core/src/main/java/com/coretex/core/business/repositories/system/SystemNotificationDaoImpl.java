package com.coretex.core.business.repositories.system;

import com.coretex.core.activeorm.dao.DefaultGenericDao;
import com.coretex.items.commerce_core_model.SystemNotificationItem;
import org.springframework.stereotype.Component;

@Component
public class SystemNotificationDaoImpl extends DefaultGenericDao<SystemNotificationItem> implements SystemNotificationDao {


	public SystemNotificationDaoImpl() {
		super(SystemNotificationItem.ITEM_TYPE);
	}
}
