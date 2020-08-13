package com.loohp.tournament.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.PlayerUtils;

public class Find implements CommandExecutor {

	@Override
	public void execute(String[] args) {
		if (args[0].equalsIgnoreCase("find")) {
			if (Arrays.asList(args).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Find.Usage"));
			} else {
				if (!TournamentServer.getInstance().hasActiveCompetition()) {
					IO.writeLn(TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning"));
					return;
				}
				
				if (args.length < 2) {
					IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Find.Usage"));
					return;
				}
				
				List<Player> players = new ArrayList<Player>();
				Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
				if (pattern.matcher(args[1]).matches()) {
					players.add(PlayerUtils.getPlayer(UUID.fromString(args[1])));
				} else {
					for (Player player : TournamentServer.getInstance().getPlayerList()) {
						if (player.getName().equals(args[1])) {
							players.add(player);
						}
					}
				}

				if (players.isEmpty()) {
					IO.writeLn(TournamentServer.getInstance().getLang().get("Common.PlayerNotFound"));
					return;
				}
				
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Find.Header"));
				IO.writeLn("----------");
				for (Player player : players) {
					IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Find.Name").replace("%s", player.getName()));
					IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Find.UUID").replace("%s", player.getId().toString()));	
					Group activeGroup = PlayerUtils.getActiveRound(player, TournamentServer.getInstance().getActiveCompetition());
					IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Find.ActiveMatch").replace("%s", TournamentServer.getInstance().getLang().get("Commands.Find.Match").replace("%s", GroupUtils.getMatchNumber(activeGroup) + "")));
					List<String> matches = new ArrayList<String>();
					for (Group group : PlayerUtils.getGroups(player, TournamentServer.getInstance().getActiveCompetition())) {
						String string = TournamentServer.getInstance().getLang().get("Commands.Find.Match").replace("%s", GroupUtils.getMatchNumber(group) + "");
						matches.add(string);
					}
					IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Find.AllMatches").replace("%s", String.join(", ", matches)));
					IO.writeLn("");
				}
			}
		}
	}

}
