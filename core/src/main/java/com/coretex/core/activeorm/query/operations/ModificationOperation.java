package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import net.sf.jsqlparser.statement.Statement;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class ModificationOperation<S extends Statement, O extends ModificationOperationSpec<S, ? extends ModificationOperation>> extends SqlOperation<S, O> {
	public ModificationOperation(O operationSpec) {
		super(operationSpec);
	}

	public void execute() {
		doTransactional(() -> {
			executeBefore();
			executeOperation();
			executeAfter();
			getOperationSpec().flush();
		});
	}


	protected void doTransactional(Runnable executor) {
		if(isTransactionInitiator() && getOperationSpec().isTransactionEnabled()){
			getJdbcService().executeInTransaction(() -> new TransactionCallbackWithoutResult() {
				@Override
				protected void doInTransactionWithoutResult(
						TransactionStatus transactionStatus) {
					try {
						executor.run();
					} catch (Exception e) {
						transactionStatus.setRollbackOnly();
						throw e;
					}
				}
			});
		}else{
			executor.run();
		}
	}

	protected abstract boolean isTransactionInitiator();

	protected abstract void executeBefore();

	public abstract void executeOperation();

	protected abstract void executeAfter();


}
