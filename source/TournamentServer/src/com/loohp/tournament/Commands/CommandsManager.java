package com.loohp.tournament.Commands;

import java.util.ArrayList;
import java.util.List;

public class CommandsManager {

	private List<CommandExecutor> executors;

	public CommandsManager() {
		this.executors = new ArrayList<>();
	}

	public void fireExecutors(String[] args) throws Exception {
		for (CommandExecutor executor : executors) {
			try {
				executor.execute(args);
			} catch (Exception e) {
				System.err.println("Error while passing command \"" + args[0] + "\" to \"" + executor.getClass().getSimpleName() + "\"");
				e.printStackTrace();
			}
		}
	}

	public void registerCommands(CommandExecutor executor) {
		executors.add(executor);
	}

	public void unregsiterCommands(CommandExecutor executor) {
		executors.removeIf(each -> each.equals(executor));
	}

}
