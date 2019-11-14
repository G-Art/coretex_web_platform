package com.coretex.search.services.workflow;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.coretex.search.services.worker.DeleteObjectWorker;
import com.coretex.search.services.worker.ExecutionContext;


@Component
public class DeleteObjectWorkflow extends Workflow {

	private static Logger log = LoggerFactory.getLogger(DeleteObjectWorkflow.class);

	private List deleteObjectWorkflow;


	public List getDeleteObjectWorkflow() {
		return deleteObjectWorkflow;
	}


	public void setDeleteObjectWorkflow(List deleteObjectWorkflow) {
		this.deleteObjectWorkflow = deleteObjectWorkflow;
	}


	public void deleteObject(String collection, String object, String id) throws Exception {


		if (deleteObjectWorkflow != null) {
			ExecutionContext context = new ExecutionContext();
			for (Object o : deleteObjectWorkflow) {
				DeleteObjectWorker iw = (DeleteObjectWorker) o;
				iw.deleteObject(super.getSearchClient(), collection, object, id, context);
			}
		}
	}

}
