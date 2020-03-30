package com.loohp.tournament.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import com.loohp.tournament.Lang;
import com.loohp.tournament.Tournament;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.PlayerUtils;

public class Find {
	
	public static void findPlayer(String[] args) {
		
		if (!Tournament.activeCompetition.isPresent()) {
			IO.writeLn(Lang.getLang("Common.CompetitionNotRunning"));
			return;
		}
		
		if (args.length < 2) {
			IO.writeLn(Lang.getLang("Commands.Find.Usage"));
			return;
		}
		
		List<Player> players = new ArrayList<Player>();
		Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
		if (pattern.matcher(args[1]).matches()) {
			players.add(PlayerUtils.getPlayer(UUID.fromString(args[1])));
		} else {
			for (Player player : Tournament.playerList) {
				if (player.getName().equals(args[1])) {
					players.add(player);
				}
			}
		}

		if (players.isEmpty()) {
			IO.writeLn(Lang.getLang("Common.PlayerNotFound"));
			return;
		}
		
		IO.writeLn(Lang.getLang("Commands.Find.Header"));
		IO.writeLn("----------");
		for (Player player : players) {
			IO.writeLn(Lang.getLang("Commands.Find.Name").replace("%s", player.getName()));
			IO.writeLn(Lang.getLang("Commands.Find.UUID").replace("%s", player.getId().toString()));	
			Group activeGroup = PlayerUtils.getActiveRound(player, Tournament.activeCompetition.get());
			IO.writeLn(Lang.getLang("Commands.Find.ActiveMatch").replace("%s", Lang.getLang("Commands.Find.Match").replace("%s", GroupUtils.getMatchNumber(activeGroup) + "")));
			List<String> matches = new ArrayList<String>();
			for (Group group : PlayerUtils.getGroups(player, Tournament.activeCompetition.get())) {
				String string = Lang.getLang("Commands.Find.Match").replace("%s", GroupUtils.getMatchNumber(group) + "");
				matches.add(string);
			}
			IO.writeLn(Lang.getLang("Commands.Find.AllMatches").replace("%s", String.join(", ", matches)));
			IO.writeLn("");
		}
		
	}

}
