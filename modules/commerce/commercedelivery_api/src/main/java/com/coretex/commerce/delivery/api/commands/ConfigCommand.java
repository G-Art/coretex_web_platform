package com.coretex.commerce.delivery.api.commands;

public interface ConfigCommand extends Command {

	<T> T getConfig(String key, T defaultValue);

	default <T> T getConfig(String key){
		return getConfig(key, null);
	}
}
