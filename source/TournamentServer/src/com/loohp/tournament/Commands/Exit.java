package com.loohp.tournament.Commands;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Database.Database;
import com.loohp.tournament.Utils.IO;

public class Exit {
	public static void exit(int num) {
		IO.writeLn("Saving data to database");
		long start = System.currentTimeMillis();
		Database.savePlayer(TournamentServer.playerList);
		Database.saveCompetition(TournamentServer.activeCompetition);
		long end = System.currentTimeMillis();
		IO.writeLn("Finished saving data to database! (" + (end - start) + "ms)");
		IO.writeLn("Press [enter] to quit");
		IO.readLn();
		System.exit(num);
	}
	
	public static void exit() {
		exit(0);
	}
}
