package com.coretex.core.activeorm.query;

public enum QueryType {
	SELECT,
	UPDATE, UPDATE_CASCADE,
	INSERT, INSERT_CASCADE,
	DELETE, DELETE_CASCADE,
	LOCALIZED_DATA_SAVE, LOCALIZED_DATA_DELETE
}
