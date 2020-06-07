package com.loohp.tournament.ClientFunctions;

import java.util.List;

import com.loohp.tournament.Lang;
import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;
import com.loohp.tournament.Server.Connection;
import com.loohp.tournament.Server.Server;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.RoundUtils;

public class FunctionSendFinalReport {
	
	public static void sendReports(Player first, Player runner, Player runner2, Player runner3) {
		String report = getReport(first, runner, runner2, runner3);
		for (Connection client : Server.clients) {
			String out = "function:finishreport=" + report;
			client.send(out);
		}
	}
	
	public static String getReport(Player first, Player runner, Player runner2, Player runner3) {
		String output = "";
		
		if (TournamentServer.playerList.size() == 0) {
			output = output + Lang.getLang("Common.NoPlayers") + "\n";	
		} else {
			output = output + Lang.getLang("Functions.ReportGenerator.PlayerList") + "\n";
			output = output + "===========================================================================================" + "\n";
			output = output + String.format("|%47s|%33s|%7s|", Lang.getLang("Common.PlayerName"), Lang.getLang("Common.School"), Lang.getLang("Common.Seeded")) + "\n";
			output = output + "===========================================================================================" + "\n";
			for (Player player : TournamentServer.playerList) {
				String name = player.getName();
				String school = player.getSchool();
				String seeded = String.valueOf(player.getSeeded());
				output = output + String.format("|%47s|%33s|%7s|", name, school, seeded);
				output = output + "" + "\n";
			}
			output = output + "===========================================================================================" + "\n";
		}

        output = output + "" + "\n";
        output = output + "" + "\n";
        output = output + "" + "\n";
		if (TournamentServer.playerList.size() == 0) {
			output = output + Lang.getLang("Common.NoPlayers") + "\n";
		} else {
			output = output + Lang.getLang("Functions.ReportGenerator.UUIDList") + "\n";
			output = output + "===========================================================================================" + "\n";
			output = output + String.format("|%47s|%41s|", Lang.getLang("Common.PlayerName"), Lang.getLang("Common.UUID")) + "\n";
			output = output + "===========================================================================================" + "\n";
			for (Player player : TournamentServer.playerList) {
				String name = player.getName();
				String id = player.getId().toString();
				output = output + String.format("|%47s|%41s|", name, id);
				output = output + "" + "\n";
			}
			output = output + "===========================================================================================" + "\n";
		}
        
		output = output + "" + "\n";
        output = output + "" + "\n";
        output = output + "" + "\n";
		if (!TournamentServer.activeCompetition.isPresent()) {
			output = output + Lang.getLang("Common.CompetitionNotRunning") + "\n";
		} else {
			Competition comp = TournamentServer.activeCompetition.get();
			output = output + Lang.getLang("Functions.ReportGenerator.CurrentStage").replace("%s", TournamentServer.activeCompetition.get().getRounds().get(TournamentServer.activeCompetition.get().getActiveRound()).getName()) + "\n";
			output = output + "" + "\n";
	        output = output + "" + "\n";
	        output = output + Lang.getLang("Functions.ReportGenerator.Fixtures") + "\n";
			for (int i = 0; i < comp.getGroups().size(); i++) {
				Group group = comp.getGroups().get(i);
				output = output + "===========================================================================================" + "\n";
				String header = Lang.getLang("Functions.ReportGenerator.Match").replace("%s", GroupUtils.getMatchNumber(group) + " - " + RoundUtils.getRoundNameFromRoundNumber(group.getRound().getRoundNumber()));
				output = output + String.format("|%-89s|", header);
				output = output + "" + "\n";
				output = output + "-------------------------------------------------------------------------------------------" + "\n";
				output = output + String.format("|%-44s|%44s|", Lang.getLang("Common.Home"), Lang.getLang("Common.Away")) + "\n";
				output = output + "-------------------------------------------------------------------------------------------" + "\n";
				String home = Lang.getLang("Common.TBD");
				if (group.getHome() != null) {
					home = group.getHome().getName();
				} else {
					if (GroupUtils.getHomeSideLastGroup(group) != null) {
						home = Lang.getLang("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
					}
				}
				String away = Lang.getLang("Common.TBD");
				if (group.getAway() != null) {
					away = group.getAway().getName();
				} else {
					if (GroupUtils.getAwaySideLastGroup(group) != null) {
						away = Lang.getLang("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
					}
				}
				if (group.getWinner().isPresent()) {
					Player winner = group.getWinner().get();
					if (winner.equals(group.getAway())) {
						away = Lang.getLang("Common.Winner") + " " + away;
					} else {
						home = home + " " + Lang.getLang("Common.Winner");
					}
				}
				output = output + String.format("|%-44s|%44s|", home, away);
				output = output + "" + "\n";
				output = output + "===========================================================================================" + "\n";
				output = output + "" + "\n";
				output = output + "" + "\n";				
			}
			
			output = output + "" + "\n";
	        output = output + "" + "\n";
	        output = output + "" + "\n";
	        
	        output = output + Lang.getLang("Functions.ReportGenerator.Rounds") + "\n";
	        for (int i = 0; i < comp.getRounds().size(); i++) {
	        	Round round = comp.getRounds().get(i);
	        	output = output + "===========================================================================================" + "\n";
				output = output + String.format("|%-89s|", round.getName());
				output = output + "" + "\n";
				output = output + "-------------------------------------------------------------------------------------------" + "\n";
				output = output + String.format("|%-44s|%44s|", Lang.getLang("Common.Home"), Lang.getLang("Common.Away")) + "\n";
				List<Group> groups = round.getGroups();
				for (Group group : groups) {
					output = output + "-------------------------------------------------------------------------------------------" + "\n";
					String home = Lang.getLang("Common.TBD");
					if (group.getHome() != null) {
						home = group.getHome().getName();
					} else {
						if (GroupUtils.getHomeSideLastGroup(group) != null) {
							home = Lang.getLang("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
						}
					}
					String away = Lang.getLang("Common.TBD");
					if (group.getAway() != null) {
						away = group.getAway().getName();
					} else {
						if (GroupUtils.getAwaySideLastGroup(group) != null) {
							away = Lang.getLang("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
						}
					}
					if (group.getWinner().isPresent()) {
						Player winner = group.getWinner().get();
						if (winner.equals(group.getAway())) {
							away = Lang.getLang("Common.Winner") + " " + away;
						} else {
							home = home + " " + Lang.getLang("Common.Winner");
						}
					}
					output = output + String.format("|%-44s|%44s|", home, away);
					output = output + "" + "\n";
				}
				output = output + "===========================================================================================" + "\n";
				output = output + "" + "\n";
		        output = output + "" + "\n";
	        }
		}
		
		output = output + "" + "\n";
		output = output + Lang.getLang("Functions.ReportGenerator.FinalResults") + "\n";
		output = output + Lang.getLang("Functions.ReportGenerator.Winner").replace("%s", first.getName()) + "\n";
		output = output + "" + "\n";
		output = output + Lang.getLang("Functions.ReportGenerator.RunnerUp").replace("%s", runner.getName()) + "\n";
		output = output + "" + "\n";
		if (runner2 != null) {
			output = output + Lang.getLang("Functions.ReportGenerator.RunnerUp2").replace("%s", runner2.getName() + (runner3 != null ? ", " : "") + (runner3 != null ? runner3.getName() : "")) + "\n";
		}
		
		return output;
	}

}
