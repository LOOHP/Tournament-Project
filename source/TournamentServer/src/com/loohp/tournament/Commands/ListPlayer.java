package com.loohp.tournament.Commands;

import java.util.Arrays;
import java.util.List;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.CustomStringUtils;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.IO;

public class ListPlayer implements CommandExecutor {
	
	public void listPlayers(String[] args) {

		if (args.length < 2) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.List.Usage"));
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
		
		if (!TournamentServer.getInstance().hasActiveCompetition()) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning"));
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
				int round = 0;
				try {
					round = Integer.valueOf(args[2]);
					round(round);
				} catch (NumberFormatException e) {
					IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.List.IntegerExpected"));
				}
			}
			return;
		}
		
		IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.List.Usage"));
	}
	
	public void currentround() {
		round(TournamentServer.getInstance().getActiveCompetition().getActiveRound());
	}
	
	public void round(int round) {
		if (!TournamentServer.getInstance().hasActiveCompetition()) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning"));
			return;
		}
		
		int maxRounds = TournamentServer.getInstance().getActiveCompetition().getRounds().size() - 1;
		if (0 > round || maxRounds < round) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.List.RoundOutOfRange").replace("%s", maxRounds + ""));
			return;
		}
		
		IO.writeLn("===========================================================================================");
		IO.writeF("|%-89s|", TournamentServer.getInstance().getActiveCompetition().getRounds().get(round).getName());
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		IO.writeF("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away"));
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		List<Group> groups = TournamentServer.getInstance().getActiveCompetition().getRounds().get(round).getGroups();
		for (Group group : groups) {
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
			IO.writeF("|%-44s|%44s|", home, away);
			IO.writeLn("");
			IO.writeLn("===========================================================================================");
		}
	}
	
	public void normalList() {
		if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.NoPlayers"));
			return;
		}
		IO.writeLn("===========================================================================================");
		IO.writeF("|%47s|%33s|%7s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.School"), TournamentServer.getInstance().getLang().get("Common.Seeded"));
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		for (Player player : TournamentServer.getInstance().getPlayerList()) {
			String name = player.getName();
			String school = player.getSchool();
			String seeded = String.valueOf(player.getSeeded());
			IO.writeF("|%47s|%33s|%7s|", name, school, seeded);
			IO.writeLn("");
		}
		IO.writeLn("===========================================================================================");
	}
	
	public void uuidList() {
		if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.NoPlayers"));
			return;
		}
		IO.writeLn("===========================================================================================");
		IO.writeF("|%47s|%41s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.UUID"));
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		for (Player player : TournamentServer.getInstance().getPlayerList()) {
			String name = player.getName();
			String id = player.getId().toString();
			IO.writeF("|%47s|%41s|", name, id);
			IO.writeLn("");
		}
		IO.writeLn("===========================================================================================");
	}
	
	public void seededList() {
		if (TournamentServer.getInstance().getPlayerList().size() == 0) {
			IO.writeLn("There are no players!");
			return;
		}
		IO.writeLn("===========================================================================================");
		IO.writeF("|%81s|%7s|", TournamentServer.getInstance().getLang().get("Common.PlayerName"), TournamentServer.getInstance().getLang().get("Common.Seeded"));
		IO.writeLn("===========================================================================================");
		for (Player player : TournamentServer.getInstance().getPlayerList()) {
			if (player.getSeeded()) {
				String name = player.getName();
				String seeded = String.valueOf(player.getSeeded());
				IO.writeF("|%81s|%7s|", name, seeded);
				IO.writeLn("");
			}
		}
		IO.writeLn("===========================================================================================");
	}

	@Override
	public void execute(String[] args) {
		if (args[0].equalsIgnoreCase("list")) {
			if (Arrays.asList(args).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.List.Usage"));
			} else {
				listPlayers(args);
			}
		}
	}
}
