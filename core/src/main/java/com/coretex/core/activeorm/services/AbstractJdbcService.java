package com.coretex.core.activeorm.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

public abstract class AbstractJdbcService {


	@Lookup
	public TransactionTemplate getTransactionTemplate() {
		return null;
	}

	@Lookup
	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return null;
	}

	public void executeInTransaction(Supplier<TransactionCallbackWithoutResult> callbackWithoutResultSupplier) {
		getTransactionTemplate().execute(callbackWithoutResultSupplier.get());
	}
}
