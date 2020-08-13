package com.loohp.tournament.Commands;

import java.util.Arrays;
import java.util.Optional;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Database.Database;
import com.loohp.tournament.Utils.IO;

public class Exit implements CommandExecutor {
	
	public static void exit(int num) {
		IO.writeLn("Saving data to database");
		long start = System.currentTimeMillis();
		Database.savePlayer(TournamentServer.getInstance().getPlayerList());
		Database.saveCompetition(Optional.ofNullable(TournamentServer.getInstance().getActiveCompetition()));
		long end = System.currentTimeMillis();
		IO.writeLn("Finished saving data to database! (" + (end - start) + "ms)");
		IO.writeLn("Press [enter] to quit");
		IO.readLn();
		System.exit(num);
	}
	
	public static void exit() {
		exit(0);
	}

	@Override
	public void execute(String[] args) {
		if (args[0].equalsIgnoreCase("exit") || args[0].equalsIgnoreCase("stop")) {
			if (Arrays.asList(args).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Exit.Usage"));
			} else {
				exit();
			}
		}
	}
}
