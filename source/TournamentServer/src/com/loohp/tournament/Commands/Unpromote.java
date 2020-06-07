package com.loohp.tournament.Commands;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import com.loohp.tournament.Lang;
import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.PlayerUtils;

public class Unpromote {
	
	public static void undo(String[] args) {
		
		if (!TournamentServer.activeCompetition.isPresent()) {
			IO.writeLn(Lang.getLang("Common.CompetitionNotRunning"));
			return;
		}
		
		if (args.length < 2) {
			IO.writeLn(Lang.getLang("Commands.Unpromote.Usage"));
			return;
		}
		
		Player player = null;
		Pattern pattern = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
		if (pattern.matcher(args[1]).matches()) {
			if (PlayerUtils.getPlayer(UUID.fromString(args[1])) != null) {
				player = PlayerUtils.getPlayer(UUID.fromString(args[1]));
			}
		}
		if (player == null) {
			if (PlayerUtils.getPlayer(args[1]) != null) {
				player = PlayerUtils.getPlayer(args[1]);
			}
		}
		if (player == null) {
			IO.writeLn(Lang.getLang("Common.PlayerNotFound"));
			return;
		}
			
		Group group = PlayerUtils.getActiveRound(player, TournamentServer.activeCompetition.get());
		
		if (group == null) {
			IO.writeLn(Lang.getLang("Commands.Unpromote.PlayerNotInAMatch"));
			return;
		}
		
		if (!group.getWinner().isPresent()) {
			IO.writeLn(Lang.getLang("Commands.Unpromote.NotYetPromoted"));
			return;
		}
		group.setWinner(Optional.empty());
		
		if (group.getNextRoundSide().get() == 0) {
			group.getNextRoundGroup().get().setHome(null);
		} else {
			group.getNextRoundGroup().get().setAway(null);
		}
		
		IO.writeLn(Lang.getLang("Commands.Unpromote.Done").replace("%s", player.getName() + " (" + player.getId().toString() + ")"));
	}

}
