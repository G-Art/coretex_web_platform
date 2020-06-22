package com.coretex.core.shell.impl;

import com.coretex.core.shell.CommandBuilder;
import com.coretex.core.shell.CommandExecutor;
import com.coretex.core.shell.CommandResult;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

public class DefaultCommandExecutor implements CommandExecutor {

	public CommandResult execute(Collection<CommandBuilder> builders) {
		ProcessBuilder processBuilder = new ProcessBuilder();
		processBuilder.redirectErrorStream(true);
		try {
			builders.forEach(builder -> builder.build(processBuilder));

			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			DefaultCommandResult commandResult;
			try {

				Process process = processBuilder.start();
				Thread streamCopyThread = new Thread(() -> {
					try {
						int chr;
						while ((chr = process.getInputStream().read()) != -1) {
							outputStream.write(chr);
						}

					} catch (IOException ioException) {
						throw new RuntimeException(ioException);
					}
				});
				streamCopyThread.start();
				streamCopyThread.join();

				process.waitFor();
				outputStream.flush();
				String output = outputStream.toString();
				commandResult = new DefaultCommandResult();
				commandResult.setOutput(output);
				commandResult.setExitValue(process.exitValue());
			} catch (Throwable throwable) {
				try {
					outputStream.close();
				} catch (Throwable var10) {
					throwable.addSuppressed(var10);
				}

				throw throwable;
			}

			outputStream.close();
			return commandResult;
		} catch (Exception e) {
			throw new RuntimeException("Error while executing command", e);
		}
	}
}
