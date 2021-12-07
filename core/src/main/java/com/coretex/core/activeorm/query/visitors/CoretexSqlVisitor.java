package com.coretex.core.activeorm.query.visitors;

import com.coretex.core.activeorm.query.SqlDynamicNamedParam;
import org.apache.calcite.sql.util.SqlVisitor;
import org.apache.calcite.sql2rel.SqlRexContext;

public interface CoretexSqlVisitor<R> extends SqlVisitor<R>, SqlRexContext {


	/**
	 * Visits a dynamic parameter.
	 *
	 * @param param Dynamic parameter
	 * @see SqlDynamicNamedParam#accept(SqlVisitor)
	 */
	R visit(SqlDynamicNamedParam param);
}
