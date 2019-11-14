package com.coretex.newpost.api.data;


import java.util.List;
import java.util.Map;

public class ResponseApiData<P> {

	private Boolean success;
	private P[] data;
	private String[] errors;

	private String[] warnings;
	private Object info;
	private String[] messageCodes;
	private String[] errorCodes;
	private String[] warningCodes;
	private String[] infoCodes;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public P[] getData() {
		return data;
	}

	public void setData(P[] data) {
		this.data = data;
	}

	public String[] getErrors() {
		return errors;
	}

	public void setErrors(String[] errors) {
		this.errors = errors;
	}

	public String[] getWarnings() {
		return warnings;
	}

	public void setWarnings(String[] warnings) {
		this.warnings = warnings;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	public String[] getMessageCodes() {
		return messageCodes;
	}

	public void setMessageCodes(String[] messageCodes) {
		this.messageCodes = messageCodes;
	}

	public String[] getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(String[] errorCodes) {
		this.errorCodes = errorCodes;
	}

	public String[] getWarningCodes() {
		return warningCodes;
	}

	public void setWarningCodes(String[] warningCodes) {
		this.warningCodes = warningCodes;
	}

	public String[] getInfoCodes() {
		return infoCodes;
	}

	public void setInfoCodes(String[] infoCodes) {
		this.infoCodes = infoCodes;
	}
}
