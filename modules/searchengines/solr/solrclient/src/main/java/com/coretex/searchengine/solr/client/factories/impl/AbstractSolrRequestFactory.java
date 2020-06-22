package com.coretex.searchengine.solr.client.factories.impl;

import com.coretex.searchengine.solr.client.CredentialProvider;
import com.coretex.searchengine.solr.client.factories.SolrRequestFactory;
import org.apache.solr.client.solrj.SolrRequest;

import java.util.Objects;

public abstract class AbstractSolrRequestFactory<P, T extends SolrRequest<?>> implements SolrRequestFactory<P, T> {

	private boolean useApiV2 = false;
	private boolean useBinaryV2 = false;
	private CredentialProvider credentialProvider;

	protected abstract T createType(P param);

	@Override
	public T create(P param) {
		var type = createType(param);
		fulfilTypeParams(type);
		return type;
	}

	protected void fulfilTypeParams(T type) {
		type.setUseV2(useApiV2);
		type.setUseBinaryV2(useBinaryV2);
		if (Objects.nonNull(credentialProvider)) {
			credentialProvider.set(type);
		}
	}

	public void setUseApiV2(boolean useApiV2) {
		this.useApiV2 = useApiV2;
	}

	public void setUseBinaryV2(boolean useBinaryV2) {
		this.useBinaryV2 = useBinaryV2;
	}

	public void setCredentialProvider(CredentialProvider credentialProvider) {
		this.credentialProvider = credentialProvider;
	}
}
