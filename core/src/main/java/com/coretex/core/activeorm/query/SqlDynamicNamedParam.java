package com.coretex.core.activeorm.query;

import com.coretex.core.activeorm.query.visitors.CoretexSqlVisitor;
import org.apache.calcite.sql.SqlDynamicParam;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlNode;
import org.apache.calcite.sql.SqlWriter;
import org.apache.calcite.sql.parser.SqlParserPos;
import org.apache.calcite.sql.util.SqlVisitor;
import org.apache.calcite.sql.validate.SqlMonotonicity;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorScope;
import org.apache.calcite.util.Litmus;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Objects;

public class SqlDynamicNamedParam extends SqlDynamicParam {
	//~ Instance fields --------------------------------------------------------

	private final String id;

	//~ Constructors -----------------------------------------------------------

	public SqlDynamicNamedParam(
			String id,
			int index,
			SqlParserPos pos) {
		super(index,pos);
		this.id= id;
	}

	//~ Methods ----------------------------------------------------------------

	@Override public SqlNode clone(SqlParserPos pos) {
		return new SqlDynamicNamedParam(id, getIndex(), pos);
	}

	@Override public SqlKind getKind() {
		return SqlKind.DYNAMIC_PARAM;
	}


	@Override public void unparse(
			SqlWriter writer,
			int leftPrec,
			int rightPrec) {
		writer.print(":"+id+" ");
		writer.setNeedWhitespace(true);
	}

	@Override public void validate(SqlValidator validator, SqlValidatorScope scope) {
		validator.validateDynamicParam(this);
	}

	@Override public SqlMonotonicity getMonotonicity(@Nullable SqlValidatorScope scope) {
		return SqlMonotonicity.CONSTANT;
	}

	@Override public <R> R accept(SqlVisitor<R> visitor) {
		if(visitor instanceof CoretexSqlVisitor){
			return ((CoretexSqlVisitor<R>)visitor).visit(this);
		}
		return visitor.visit(this);
	}

	@Override public boolean equalsDeep(@Nullable SqlNode node, Litmus litmus) {
		if (!(node instanceof SqlDynamicNamedParam)) {
			return litmus.fail("{} != {}", this, node);
		}
		SqlDynamicNamedParam that = (SqlDynamicNamedParam) node;
		if (!Objects.equals(this.id, that.id)) {
			return litmus.fail("{} != {}", this, node);
		}
		return litmus.succeed();
	}

	public String getId() {
		return id;
	}
}