package com.coretex.core.activeorm.query.select.scanners;

import com.coretex.core.activeorm.exceptions.QueryException;
import com.coretex.core.activeorm.query.select.data.AliasInfoHolder;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.select.WithItem;

import java.util.Objects;

public class SubSelectScanner<Q> extends Scanner<SubSelect, Q> {

	private SubSelect subSelect;

	private SelectBodyScanner<SubSelect> selectBodyScanner;

	public SubSelectScanner(int deep, Q parentStatement) {
		super(deep, parentStatement);
	}

	public SubSelectScanner(int deep, Q parentStatement, Scanner parent) {
		super(deep, parentStatement, parent);
	}

	@Override
	public void scan(SubSelect query) {

		Scanner prnt = getParent();
		while (Objects.nonNull(prnt)){
			if(prnt instanceof SelectBodyScanner){
				((SelectBodyScanner) prnt).addSubSelectScanners(this);
				if(Objects.nonNull(query.getAlias())){
					((SelectBodyScanner) prnt).addAliasInfoHolder(query.getAlias().getName(), new AliasInfoHolder<>(query.getAlias().getName(), this));
				}

				prnt=null;
			}else{
				prnt = prnt.getParent();
			}
		}

		this.subSelect = query;
		query.getSelectBody().accept(this);
	}

	@Override
	public SubSelect scannedObject() {
		return subSelect;
	}

	@Override
	public Class<? extends SubSelect> scannedObjectClass() {
		return subSelect.getClass();
	}

	@Override
	public void visit(SubSelect subSelect) {
		if (subSelect.getWithItemsList() != null) {
			throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
//			for (WithItem withItem : subSelect.getWithItemsList()) {
//				withItem.accept(this);
//			}
		}
		if (subSelect.getSelectBody() instanceof WithItem) {
			throw new QueryException(NOT_SUPPORTED_YET + " With items query expressions.");
		}
		selectBodyScanner = new SelectBodyScanner<>(getDeep()+1, subSelect, this);
		selectBodyScanner.scan(subSelect.getSelectBody());
	}


	public SelectBodyScanner<SubSelect> getSelectBodyScanner() {
		return selectBodyScanner;
	}
}
