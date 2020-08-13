package com.loohp.tournament.Commands;

import java.util.Arrays;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.RoundCheck;

public class Restart implements CommandExecutor {
	
	public void restart(String[] args) {
		if (!TournamentServer.getInstance().hasActiveCompetition()) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning"));
			return;
		}
		
		IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Restart.Confirmation"));
		IO.writeLn("(YES or NO)");
		IO.write("[!]> ");
		String[] inp = IO.readLn();
		if (inp[0].equalsIgnoreCase("YES")) {
			TournamentServer.getInstance().setActiveCompetition(null);
			try {
				TournamentServer.getInstance().getCommandsManager().fireExecutors(new String[] {"start"});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		
		IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Restart.Cancel"));
	}

	@Override
	public void execute(String[] args) {
		if (args[0].equalsIgnoreCase("restart")) {
			if (Arrays.asList(args).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Restart.Usage"));
			} else {
				restart(args);
				RoundCheck.check();
			}
			
		}
	}
	
	
}
