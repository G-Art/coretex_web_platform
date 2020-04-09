package com.coretex.commerce.delivery.api.commands.handler;

import com.coretex.commerce.delivery.api.commands.ActionCommand;
import com.coretex.commerce.delivery.api.commands.ConfigCommand;

import java.util.Map;

public class DefaultDeliveryServiceCommandHandler implements CommandHandler {



	private Map<String, Map<String, ActionCommand>> actionCommands;
	private Map<String, Map<String, ConfigCommand>> configCommands;
}
