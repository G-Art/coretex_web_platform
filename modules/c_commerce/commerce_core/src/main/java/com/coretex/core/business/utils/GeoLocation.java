package com.coretex.core.business.utils;

import com.coretex.core.model.common.Address;

public interface GeoLocation {

	Address getAddress(String ipAddress) throws Exception;

}
