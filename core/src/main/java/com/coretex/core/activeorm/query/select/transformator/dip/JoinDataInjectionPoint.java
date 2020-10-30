package com.coretex.core.activeorm.query.select.transformator.dip;

import com.coretex.core.activeorm.query.QueryStatementContext;
import com.coretex.core.activeorm.query.select.scanners.JoinScanner;
import com.coretex.core.activeorm.query.select.scanners.SelectBodyScanner;
import net.sf.jsqlparser.statement.Statement;

import java.util.Optional;

public class JoinDataInjectionPoint extends AbstractScannerDataInjectionPoint<JoinScanner> {

	public JoinDataInjectionPoint(JoinScanner scanner, QueryStatementContext<? extends Statement> context) {
		super(scanner, context);
	}

	public void setSelectBodyScannerOwner(SelectBodyScanner selectBodyScannerOwner){
		this.addParam(SELECT_BODY_OWNER, selectBodyScannerOwner);
	}

	@Override
	public Optional<SelectBodyScanner> getSelectBodyScannerOwner(){
		return super.getSelectBodyScannerOwner();
	}
}
