package com.loohp.tournament.Commands;

import java.util.Optional;

import com.loohp.tournament.Lang;
import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Utils.IO;

public class Restart {
	
	public static void restart(String[] args) {
		if (!TournamentServer.activeCompetition.isPresent()) {
			IO.writeLn(Lang.getLang("Common.CompetitionNotRunning"));
			return;
		}
		
		IO.writeLn(Lang.getLang("Commands.Restart.Confirmation"));
		IO.writeLn("(YES or NO)");
		IO.write("[!]> ");
		String[] inp = IO.readLn();
		if (inp[0].equalsIgnoreCase("YES")) {
			TournamentServer.activeCompetition = Optional.empty();
			Start.startCompetition();
			return;
		}
		
		IO.writeLn(Lang.getLang("Commands.Restart.Cancel"));
	}
}
