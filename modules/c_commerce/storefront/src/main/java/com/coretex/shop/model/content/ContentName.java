package com.coretex.shop.model.content;

/**
 * Input Object used in REST request
 *
 * @author carlsamson
 */
public class ContentName extends Content {


	private static final long serialVersionUID = 1L;

	public ContentName() {
		super();
	}

	public ContentName(String name) {
		super(name);
	}

	public ContentName(String name, String contentType) {
		super(name);
	}


}
