package com.coretex.newpost.api.enchiridion.data.values;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCodeTextValue {

	@JsonProperty(value = "MessageCode")
	private String messageCode;

	@JsonProperty(value = "MessageText")
	private String messageText;

	@JsonProperty(value = "MessageDescriptionRU")
	private String messageDescriptionRU;

	@JsonProperty(value = "MessageDescriptionUA")
	private String messageDescriptionUA;

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getMessageText() {
		return messageText;
	}

	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}

	public String getMessageDescriptionRU() {
		return messageDescriptionRU;
	}

	public void setMessageDescriptionRU(String messageDescriptionRU) {
		this.messageDescriptionRU = messageDescriptionRU;
	}

	public String getMessageDescriptionUA() {
		return messageDescriptionUA;
	}

	public void setMessageDescriptionUA(String messageDescriptionUA) {
		this.messageDescriptionUA = messageDescriptionUA;
	}
}
