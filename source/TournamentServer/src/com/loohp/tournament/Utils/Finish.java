package com.loohp.tournament.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.loohp.tournament.Lang;
import com.loohp.tournament.Tournament;
import com.loohp.tournament.ClientFunctions.FunctionSendFinalReport;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;

public class Finish {
	
	public static void finish() {
		try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {}
		IO.writeLn("===========================================================================================");
		IO.writeLn("===========================================================================================");
		IO.writeLn(Lang.getLang("Functions.Finish.CompetitionComplete"));
		IO.writeLn("===========================================================================================");
		List<Player> top4 = new ArrayList<Player>();
		Player first = Tournament.activeCompetition.get().getGroups().get(Tournament.activeCompetition.get().getGroups().size() - 1).getWinner().get();
		top4.add(first);
		Player runner = null;
		if (Tournament.activeCompetition.get().getGroups().get(Tournament.activeCompetition.get().getGroups().size() - 1).getHome().equals(first)) {
			runner = Tournament.activeCompetition.get().getGroups().get(Tournament.activeCompetition.get().getGroups().size() - 1).getAway();
		} else {
			runner = Tournament.activeCompetition.get().getGroups().get(Tournament.activeCompetition.get().getGroups().size() - 1).getHome();
		}
		top4.add(runner);
		Player runner2 = null;
		Round semi = Tournament.activeCompetition.get().getRounds().get(Tournament.activeCompetition.get().getRounds().size() - 2);
		for (Player player : RoundUtils.getPlayersInRound(semi)) {
			if (!top4.contains(player)) {
				runner2 = player;
				top4.add(player);
				break;
			}
		}
		Player runner3 = null;
		for (Player player : RoundUtils.getPlayersInRound(semi)) {
			if (!top4.contains(player)) {
				runner3 = player;
				top4.add(player);
				break;
			}
		}
		IO.writeLn(Lang.getLang("Functions.Finish.Winner").replace("%s", first.getName()));
		IO.writeLn("");
		IO.writeLn(Lang.getLang("Functions.Finish.RunnerUp").replace("%s", runner.getName()));
		IO.writeLn("");
		IO.writeLn(Lang.getLang("Functions.Finish.RunnerUp2").replace("%s", runner2.getName() + ", " + runner3.getName()));
		
		IO.writeLn("");
		IO.writeLn("");
		IO.writeLn(Lang.getLang("Functions.Finish.PrintingReport"));
		ReportGenerator.finish(Optional.empty(), first, runner, runner2, runner3);
		FunctionSendFinalReport.sendReports(first, runner, runner2, runner3);
		
		IO.writeLn(Lang.getLang("Functions.Finish.SettingTop4Seeded"));
		for (Player player : Tournament.playerList) {
			player.setSeeded(false);
		}
		for (Player player : top4) {
			player.setSeeded(true);
		}
		Tournament.activeCompetition = Optional.empty();
		IO.writeLn(Lang.getLang("Functions.Finish.Reset"));
	}
}
