package com.loohp.tournament.ClientFunctions;

import java.util.List;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Packets.PacketOutReportNormal;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;
import com.loohp.tournament.Server.ClientConnection;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.RoundUtils;

public class FunctionGetReport {
	
	public static void GenAndSendReport(ClientConnection client) {
		
		String output = "";
		
        if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			output = output + TournamentServer.getInstance().getLang().get("Common.NoPlayers") + "\n";	
		} else {
			output = output + TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.PlayerList") + "\n";
			output = output + "===========================================================================================" + "\n";
			output = output + String.format("|%47s|%33s|%7s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.School"), TournamentServer.getInstance().getLang().get("Common.Seeded")) + "\n";
			output = output + "===========================================================================================" + "\n";
			for (Player player : TournamentServer.getInstance().getPlayerList()) {
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
		if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			output = output + TournamentServer.getInstance().getLang().get("Common.NoPlayers") + "\n";
		} else {
			output = output + TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.UUIDList") + "\n";
			output = output + "===========================================================================================" + "\n";
			output = output + String.format("|%47s|%41s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.UUID")) + "\n";
			output = output + "===========================================================================================" + "\n";
			for (Player player : TournamentServer.getInstance().getPlayerList()) {
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
		if (!TournamentServer.getInstance().hasActiveCompetition()) {
			output = output + TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning") + "\n";
		} else {
			Competition comp = TournamentServer.getInstance().getActiveCompetition();
			output = output + TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.CurrentStage").replace("%s", comp.getRounds().get(comp.getActiveRound()).getName()) + "\n";
			output = output + "" + "\n";
	        output = output + "" + "\n";
	        output = output + TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Fixtures") + "\n";
			for (int i = 0; i < comp.getGroups().size(); i++) {
				Group group = comp.getGroups().get(i);
				output = output + "===========================================================================================" + "\n";
				String header = TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Match").replace("%s", GroupUtils.getMatchNumber(group) + " - " + RoundUtils.getRoundNameFromRoundNumber(group.getRound().getRoundNumber()));
				output = output + String.format("|%-89s|", header);
				output = output + "" + "\n";
				output = output + "-------------------------------------------------------------------------------------------" + "\n";
				output = output + String.format("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away")) + "\n";
				output = output + "-------------------------------------------------------------------------------------------" + "\n";
				String home = TournamentServer.getInstance().getLang().get("Common.TBD");
				if (group.getHome() != null) {
					home = group.getHome().getName();
				} else {
					if (GroupUtils.getHomeSideLastGroup(group) != null) {
						home = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
					}
				}
				String away = TournamentServer.getInstance().getLang().get("Common.TBD");
				if (group.getAway() != null) {
					away = group.getAway().getName();
				} else {
					if (GroupUtils.getAwaySideLastGroup(group) != null) {
						away = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
					}
				}
				if (group.getWinner().isPresent()) {
					Player winner = group.getWinner().get();
					if (winner.equals(group.getAway())) {
						away = TournamentServer.getInstance().getLang().get("Common.Winner") + " " + away;
					} else {
						home = home + " " + TournamentServer.getInstance().getLang().get("Common.Winner");
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
	        
	        output = output + TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Rounds") + "\n";
	        for (int i = 0; i < comp.getRounds().size(); i++) {
	        	Round round = comp.getRounds().get(i);
	        	output = output + "===========================================================================================" + "\n";
				output = output + String.format("|%-89s|", round.getName());
				output = output + "" + "\n";
				output = output + "-------------------------------------------------------------------------------------------" + "\n";
				output = output + String.format("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away")) + "\n";
				List<Group> groups = round.getGroups();
				for (Group group : groups) {
					output = output + "-------------------------------------------------------------------------------------------" + "\n";
					String home = TournamentServer.getInstance().getLang().get("Common.TBD");
					if (group.getHome() != null) {
						home = group.getHome().getName();
					} else {
						if (GroupUtils.getHomeSideLastGroup(group) != null) {
							home = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getHomeSideLastGroup(group)) + "");
						}
					}
					String away = TournamentServer.getInstance().getLang().get("Common.TBD");
					if (group.getAway() != null) {
						away = group.getAway().getName();
					} else {
						if (GroupUtils.getAwaySideLastGroup(group) != null) {
							away = TournamentServer.getInstance().getLang().get("Common.WinnerOfMatch").replace("%s", GroupUtils.getMatchNumber(GroupUtils.getAwaySideLastGroup(group)) + "");
						}
					}
					if (group.getWinner().isPresent()) {
						Player winner = group.getWinner().get();
						if (winner.equals(group.getAway())) {
							away = TournamentServer.getInstance().getLang().get("Common.Winner") + " " + away;
						} else {
							home = home + " " + TournamentServer.getInstance().getLang().get("Common.Winner");
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
		output = output + TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.FinalResults") + "\n";
		output = output + TournamentServer.getInstance().getLang().get("Functions.ReportGenerator.Ongoing") + "\n";
		
		client.send(new PacketOutReportNormal(output));
	}

}
