package com.coretex.commerce.delivery.api.commands;

public interface ActionCommand extends Command {

	<R,T> R perform(T value);
}
