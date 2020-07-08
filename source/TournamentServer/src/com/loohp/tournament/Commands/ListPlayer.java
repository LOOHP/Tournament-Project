package com.loohp.tournament.Commands;

import java.util.List;

import com.loohp.tournament.Lang;
import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.CustomIntegerUtils;
import com.loohp.tournament.Utils.CustomStringUtils;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.IO;

public class ListPlayer {
	
	public static void listPlayers(String[] args) {

		if (args.length < 2) {
			IO.writeLn(Lang.getLang("Commands.List.Usage"));
			return;
		}
		
		if (args[1].equalsIgnoreCase("players")) {
			if (CustomStringUtils.arrayContains("-i", args) || CustomStringUtils.arrayContains("--uuid", args)) {
				uuidList();
			} else if (CustomStringUtils.arrayContains("-s", args) || CustomStringUtils.arrayContains("--seeded", args)) {
				seededList();
			} else {
				normalList();
			}
			return;
		}
		
		if (!TournamentServer.activeCompetition.isPresent()) {
			IO.writeLn(Lang.getLang("Common.CompetitionNotRunning"));
			return;
		}
		
		if (args[1].equalsIgnoreCase("current")) {
			currentround();
			return;
		}
		
		if (args[1].equalsIgnoreCase("round")) {
			if (args.length < 3) {
				currentround();
			} else {
				if (CustomIntegerUtils.isInteger(args[2])) {
					round(Integer.valueOf(args[2]));
				} else {
					IO.writeLn(Lang.getLang("Commands.List.IntegerExpected"));
				}
			}
			return;
		}
		
		IO.writeLn(Lang.getLang("Commands.List.Usage"));
	}
	
	public static void currentround() {
		round(TournamentServer.activeCompetition.get().getActiveRound());
	}
	
	public static void round(int round) {
		if (!TournamentServer.activeCompetition.isPresent()) {
			IO.writeLn(Lang.getLang("Common.CompetitionNotRunning"));
			return;
		}
		
		int maxRounds = TournamentServer.activeCompetition.get().getRounds().size() - 1;
		if (0 > round || maxRounds < round) {
			IO.writeLn(Lang.getLang("Commands.List.RoundOutOfRange").replace("%s", maxRounds + ""));
			return;
		}
		
		IO.writeLn("===========================================================================================");
		IO.writeF("|%-89s|", TournamentServer.activeCompetition.get().getRounds().get(round).getName());
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		IO.writeF("|%-44s|%44s|", Lang.getLang("Common.Home"), Lang.getLang("Common.Away"));
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		List<Group> groups = TournamentServer.activeCompetition.get().getRounds().get(round).getGroups();
		for (Group group : groups) {
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
			IO.writeF("|%-44s|%44s|", home, away);
			IO.writeLn("");
			IO.writeLn("===========================================================================================");
		}
	}
	
	public static void normalList() {
		if (TournamentServer.playerList.size() == 0) {
			IO.writeLn(Lang.getLang("Common.NoPlayers"));
			return;
		}
		IO.writeLn("===========================================================================================");
		IO.writeF("|%47s|%33s|%7s|", Lang.getLang("Common.PlayerName"), Lang.getLang("Common.School"), Lang.getLang("Common.Seeded"));
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		for (Player player : TournamentServer.playerList) {
			String name = player.getName();
			String school = player.getSchool();
			String seeded = String.valueOf(player.getSeeded());
			IO.writeF("|%47s|%33s|%7s|", name, school, seeded);
			IO.writeLn("");
		}
		IO.writeLn("===========================================================================================");
	}
	
	public static void uuidList() {
		if (TournamentServer.playerList.size() == 0) {
			IO.writeLn(Lang.getLang("Common.NoPlayers"));
			return;
		}
		IO.writeLn("===========================================================================================");
		IO.writeF("|%47s|%41s|", Lang.getLang("Common.PlayerName"), Lang.getLang("Common.UUID"));
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		for (Player player : TournamentServer.playerList) {
			String name = player.getName();
			String id = player.getId().toString();
			IO.writeF("|%47s|%41s|", name, id);
			IO.writeLn("");
		}
		IO.writeLn("===========================================================================================");
	}
	
	public static void seededList() {
		if (TournamentServer.playerList.size() == 0) {
			IO.writeLn("There are no players!");
			return;
		}
		IO.writeLn("===========================================================================================");
		IO.writeF("|%81s|%7s|", Lang.getLang("Common.PlayerName"), Lang.getLang("Common.Seeded"));
		IO.writeLn("===========================================================================================");
		for (Player player : TournamentServer.playerList) {
			if (player.getSeeded()) {
				String name = player.getName();
				String seeded = String.valueOf(player.getSeeded());
				IO.writeF("|%81s|%7s|", name, seeded);
				IO.writeLn("");
			}
		}
		IO.writeLn("===========================================================================================");
	}
}
