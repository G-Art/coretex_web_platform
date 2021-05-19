package com.coretex.core.activeorm.query.operations;

import com.coretex.core.activeorm.query.ActiveOrmOperationExecutor;
import com.coretex.core.activeorm.query.operations.contexts.OperationConfigContext;
import com.coretex.core.activeorm.query.specs.ModificationOperationSpec;
import com.coretex.core.activeorm.services.AbstractJdbcService;
import com.coretex.core.activeorm.services.ItemOperationInterceptorService;
import net.sf.jsqlparser.statement.Statement;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.stream.Stream;

public abstract class ModificationOperation<
		S extends Statement,
		OS extends ModificationOperationSpec<S, OS, CTX>,
		CTX extends OperationConfigContext<S, OS, CTX>>
		extends SqlOperation<S, OS, CTX> {

	private AbstractJdbcService abstractJdbcService;
	private ItemOperationInterceptorService itemOperationInterceptorService;

	public ModificationOperation(AbstractJdbcService abstractJdbcService, ItemOperationInterceptorService itemOperationInterceptorService) {
		this.abstractJdbcService = abstractJdbcService;
		this.itemOperationInterceptorService = itemOperationInterceptorService;
	}

	@Override
	public <T> Stream<T> execute(CTX operationConfigContext) {
		if (useInterceptors(operationConfigContext)) {
			onPrepare(operationConfigContext);
		}
		doTransactional(operationConfigContext, () -> {
			executeBefore(operationConfigContext);
			executeOperation(operationConfigContext);
			executeAfter(operationConfigContext);
			operationConfigContext.getOperationSpec().flush();
		});
		return Stream.empty();
	}


	protected void doTransactional(CTX operationConfigContext, Runnable executor) {
		var operationSpec = operationConfigContext.getOperationSpec();
		if (isTransactionInitiator() && operationSpec.isTransactionEnabled()) {
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
		} else {
			executor.run();
		}
	}

	@Lookup
	protected ActiveOrmOperationExecutor getActiveOrmOperationExecutor() {
		return null;
	}

	protected AbstractJdbcService getJdbcService() {
		return abstractJdbcService;
	}

	protected void onPrepare(CTX operationConfigContext) {
		itemOperationInterceptorService.onSavePrepare(operationConfigContext.getOperationSpec().getItem());
	}

	protected boolean useInterceptors(CTX operationConfigContext) {
		return true;
	}

	protected ItemOperationInterceptorService getItemOperationInterceptorService() {
		return itemOperationInterceptorService;
	}

	protected abstract boolean isTransactionInitiator();

	protected abstract void executeBefore(CTX operationConfigContext);

	public abstract void executeOperation(CTX operationConfigContext);

	protected abstract void executeAfter(CTX operationConfigContext);


}
