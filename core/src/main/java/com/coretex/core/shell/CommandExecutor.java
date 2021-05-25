package com.coretex.core.shell;

import java.util.Collection;

public interface CommandExecutor {
	CommandResult execute(Collection<CommandBuilder> commandBuilders);
}
