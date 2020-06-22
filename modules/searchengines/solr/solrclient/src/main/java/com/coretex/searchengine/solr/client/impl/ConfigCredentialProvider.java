package com.coretex.searchengine.solr.client.impl;

import com.coretex.searchengine.solr.client.CredentialProvider;
import org.apache.solr.client.solrj.SolrRequest;

public class ConfigCredentialProvider implements CredentialProvider {

	private String user;
	private String password;

	@Override
	public void set(SolrRequest<?> solrRequest) {
		solrRequest.setBasicAuthCredentials(user, password);
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
