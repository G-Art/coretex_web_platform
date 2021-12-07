package com.coretex.core.activeorm.query.select;

import com.coretex.core.activeorm.query.CoretexSqlToRelConverter;
import com.coretex.core.activeorm.query.rex.CoretexRexBuilder;
import com.coretex.core.services.bootstrap.dialect.DbDialectService;
import com.coretex.core.services.bootstrap.impl.CoretexContext;
import com.google.common.collect.Lists;
import org.apache.calcite.config.CalciteConnectionConfig;
import org.apache.calcite.jdbc.CalciteSchema;
import org.apache.calcite.plan.Contexts;
import org.apache.calcite.plan.RelOptCluster;
import org.apache.calcite.plan.RelOptPlanner;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.plan.hep.HepPlanner;
import org.apache.calcite.plan.hep.HepProgramBuilder;
import org.apache.calcite.prepare.Prepare;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.sql.SqlAggFunction;
import org.apache.calcite.sql.SqlFunctionCategory;
import org.apache.calcite.sql.SqlKind;
import org.apache.calcite.sql.SqlOperatorTable;
import org.apache.calcite.sql.fun.SqlStdOperatorTable;
import org.apache.calcite.sql.type.OperandTypes;
import org.apache.calcite.sql.type.ReturnTypes;
import org.apache.calcite.sql.type.SqlOperandTypeInference;
import org.apache.calcite.sql.type.SqlTypeName;
import org.apache.calcite.sql.util.SqlOperatorTables;
import org.apache.calcite.sql.validate.SqlValidator;
import org.apache.calcite.sql.validate.SqlValidatorUtil;
import org.apache.calcite.sql2rel.SqlToRelConverter;
import org.apache.calcite.sql2rel.StandardConvertletTable;
import org.apache.calcite.util.Optionality;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;

import static com.coretex.core.activeorm.query.select.rules.CoretexRules.CORETEX_ITEM_TO_TABLE_RULE;
import static com.coretex.core.activeorm.query.select.rules.CoretexRules.CORETEX_MYSQL_BIN_TO_UUID_RULE;
import static com.coretex.core.activeorm.query.select.rules.CoretexRules.CORETEX_MYSQL_UUID_TO_BIN_RULE;
import static org.apache.calcite.rel.rules.CoreRules.FILTER_MERGE;
import static org.apache.calcite.rel.rules.CoreRules.FILTER_SET_OP_TRANSPOSE;
import static org.apache.calcite.rel.rules.CoreRules.PROJECT_REMOVE;

public class QueryOptimizerConfigFactory {

	private Prepare.CatalogReader catalogReader;
	private RelDataTypeFactory typeFactory;
	private CalciteConnectionConfig config;

	public static final SqlAggFunction MYSQL_BIN_TO_UUID_FUNCTION = new MySqlUUIDSqlAggFunction("BIN_TO_UUID");
	public static final SqlAggFunction MYSQL_UUID_TO_BIN_FUNCTION = new MySqlUUIDSqlAggFunction("UUID_TO_BIN");

	public QueryOptimizerConfigFactory(Prepare.CatalogReader catalogReader,
	                                   RelDataTypeFactory typeFactory,
	                                   CalciteConnectionConfig config) {
		this.catalogReader = catalogReader;
		this.typeFactory = typeFactory;
		this.config = config;
	}

	public static CalciteSchema creteRootSchema(String name, Schema schema) {
		return CalciteSchema.createRootSchema(false, false, name, schema);
	}


	public SqlValidator createSqlValidator() {
		SqlStdOperatorTable sqlStdOperatorTable = SqlStdOperatorTable.instance();
		sqlStdOperatorTable.register(MYSQL_BIN_TO_UUID_FUNCTION);
		sqlStdOperatorTable.register(MYSQL_UUID_TO_BIN_FUNCTION);
		SqlOperatorTable operatorTable = SqlOperatorTables.chain(sqlStdOperatorTable);

		SqlValidator.Config validatorConfig = SqlValidator.Config.DEFAULT
				.withLenientOperatorLookup(config.lenientOperatorLookup())
				.withSqlConformance(config.conformance())
				.withDefaultNullCollation(config.defaultNullCollation())
				.withIdentifierExpansion(true);

		return SqlValidatorUtil.newValidator(
				operatorTable,
				this.catalogReader,
				this.typeFactory,
				validatorConfig
		);
	}

	public RelOptPlanner createPlanner(CoretexContext coretexContext) {
		List<RelOptRule> rules = Lists.newLinkedList();
		commonRules(rules);
		dialectRules(rules, coretexContext.getDbDialectService());

		return new HepPlanner(new HepProgramBuilder()
				.addRuleCollection(rules)
				.build(),
				Contexts.of(config)
		);
	}

	private void dialectRules(List<RelOptRule> rules, DbDialectService dbDialectService) {
		switch (dbDialectService.getDatabaseProduct()) {
			case MYSQL -> {
				rules.add(CORETEX_MYSQL_UUID_TO_BIN_RULE);
				rules.add(CORETEX_MYSQL_BIN_TO_UUID_RULE);
			}
			default -> {
				//ignore
			}
		}
	}

	protected void commonRules(List<RelOptRule> rules) {
		rules.add(CORETEX_ITEM_TO_TABLE_RULE);
		rules.add(FILTER_SET_OP_TRANSPOSE);
		rules.add(PROJECT_REMOVE);
		rules.add(FILTER_MERGE);

	}


	public SqlToRelConverter createSqlToRelConverter(RelOptPlanner planner, SqlValidator validator) {

		RelOptCluster cluster = RelOptCluster.create(
				planner,
				new CoretexRexBuilder(typeFactory)
		);

		SqlToRelConverter.Config converterConfig = SqlToRelConverter.config()
				.withTrimUnusedFields(true)
				.withExpand(false);

		return new CoretexSqlToRelConverter(
				null,
				validator,
				catalogReader,
				cluster,
				StandardConvertletTable.INSTANCE,
				converterConfig
		);
	}


	private static final class MySqlUUIDSqlAggFunction extends SqlAggFunction {
		public static final SqlOperandTypeInference ANY_NULLABLE =
				(callBinding, returnType, operandTypes) -> {
					RelDataTypeFactory typeFactory = callBinding.getTypeFactory();
					for (int i = 0; i < operandTypes.length; ++i) {
						operandTypes[i] =
								typeFactory.createTypeWithNullability(
										typeFactory.createSqlType(SqlTypeName.ANY), true);
					}
				};

		@Override
		public boolean isAggregator() {
			return false;
		}

		@Override
		public @Nullable SqlOperandTypeInference getOperandTypeInference() {
			return super.getOperandTypeInference();
		}

		public MySqlUUIDSqlAggFunction(String name) {
			super(name,
					null,
					SqlKind.OTHER_FUNCTION,
					ReturnTypes.VARCHAR_4,
					ANY_NULLABLE,
					OperandTypes.ANY,
					SqlFunctionCategory.STRING,
					false,
					false,
					Optionality.OPTIONAL);


		}
	}
}
