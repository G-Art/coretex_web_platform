package com.coretex.core.shell.impl;

import com.coretex.core.shell.CommandResult;

public class DefaultCommandResult implements CommandResult {
	private int exitValue;
	private String output;

	public int getExitValue() {
		return this.exitValue;
	}

	public void setExitValue(int exitValue) {
		this.exitValue = exitValue;
	}

	public String getOutput() {
		return this.output;
	}

	public void setOutput(String output) {
		this.output = output;
	}
}
