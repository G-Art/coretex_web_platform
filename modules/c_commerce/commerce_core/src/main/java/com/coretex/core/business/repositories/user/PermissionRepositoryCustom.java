package com.coretex.core.business.repositories.user;

import com.coretex.core.model.user.PermissionCriteria;
import com.coretex.core.model.user.PermissionList;


public interface PermissionRepositoryCustom {

	PermissionList listByCriteria(PermissionCriteria criteria);


}
