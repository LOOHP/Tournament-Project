package com.loohp.tournament.Commands;

import java.util.Arrays;
import java.util.Optional;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.ReportGenerator;

public class Report implements CommandExecutor {
	
	public void generate(String[] args) {		
		if (args.length == 1) {
			ReportGenerator.output(Optional.empty());
		} else {
			ReportGenerator.output(Optional.of(args[1]));
		}
	}

	@Override
	public void execute(String[] args) {
		if (args[0].equalsIgnoreCase("report")) {
			if (Arrays.asList(args).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Report.Usage"));
			} else {
				generate(args);
			}
			
		}
	}

}
