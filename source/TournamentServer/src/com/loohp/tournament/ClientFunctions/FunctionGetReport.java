package com.loohp.tournament.ClientFunctions;

import java.util.List;

import com.loohp.tournament.Lang;
import com.loohp.tournament.Tournament;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;
import com.loohp.tournament.Server.Connection;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.RoundUtils;

public class FunctionGetReport {
	
	public static void GenAndSendReport(Connection client) {
		
		String output = "";
		
        if (Tournament.playerList.size() == 0) {
			output = output + Lang.getLang("Common.NoPlayers") + "\n";	
		} else {
			output = output + Lang.getLang("Functions.ReportGenerator.PlayerList") + "\n";
			output = output + "===========================================================================================" + "\n";
			output = output + String.format("|%47s|%33s|%7s|", Lang.getLang("Common.PlayerName"), Lang.getLang("Common.School"), Lang.getLang("Common.Seeded")) + "\n";
			output = output + "===========================================================================================" + "\n";
			for (Player player : Tournament.playerList) {
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
		if (Tournament.playerList.size() == 0) {
			output = output + Lang.getLang("Common.NoPlayers") + "\n";
		} else {
			output = output + Lang.getLang("Functions.ReportGenerator.UUIDList") + "\n";
			output = output + "===========================================================================================" + "\n";
			output = output + String.format("|%47s|%41s|", Lang.getLang("Common.PlayerName"), Lang.getLang("Common.UUID")) + "\n";
			output = output + "===========================================================================================" + "\n";
			for (Player player : Tournament.playerList) {
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
		if (!Tournament.activeCompetition.isPresent()) {
			output = output + Lang.getLang("Common.CompetitionNotRunning") + "\n";
		} else {
			Competition comp = Tournament.activeCompetition.get();
			output = output + Lang.getLang("Functions.ReportGenerator.CurrentStage").replace("%s", Tournament.activeCompetition.get().getRounds().get(Tournament.activeCompetition.get().getActiveRound()).getName()) + "\n";
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
		output = output + Lang.getLang("Functions.ReportGenerator.Ongoing") + "\n";
		
		client.send("function:getreport=" + output);
	}

}
