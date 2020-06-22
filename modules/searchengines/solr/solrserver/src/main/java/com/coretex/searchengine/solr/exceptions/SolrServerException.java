package com.coretex.searchengine.solr.exceptions;

public class SolrServerException extends Exception {
	public SolrServerException() {
	}

	public SolrServerException(String message) {
		super(message);
	}

	public SolrServerException(String message, Throwable cause) {
		super(message, cause);
	}

	public SolrServerException(Throwable cause) {
		super(cause);
	}
}
