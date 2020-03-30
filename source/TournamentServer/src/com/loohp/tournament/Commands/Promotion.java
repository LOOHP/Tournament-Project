package com.loohp.tournament.Commands;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import com.loohp.tournament.Lang;
import com.loohp.tournament.Tournament;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.Finish;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.PlayerUtils;

public class Promotion {
	
	public static void promote(String[] args) {
		
		if (!Tournament.activeCompetition.isPresent()) {
			IO.writeLn(Lang.getLang("Common.CompetitionNotRunning"));
			return;
		}
		
		if (args.length < 2) {
			IO.writeLn(Lang.getLang("Commands.Promote.Usage"));
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
			
		Group group = PlayerUtils.getActiveRound(player, Tournament.activeCompetition.get());
		
		if (group == null) {
			IO.writeLn(Lang.getLang("Commands.Promote.PlayerNotInAMatch"));
			return;
		}
		
		group.setWinner(Optional.of(player));
		IO.writeLn("===========================================================================================");
		String header = Lang.getLang("Commands.Promote.Header").replace("%s", GroupUtils.getMatchNumber(group) + "");
		IO.writeF("|%-89s|", header);
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		IO.writeF("|%-44s|%44s|", Lang.getLang("Common.Home"), Lang.getLang("Common.Away"));
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		String home = "TBD";
		if (group.getHome() != null) {
			home = group.getHome().getName();
		}
		String away = "TBD";
		if (group.getAway() != null) {
			away = group.getAway().getName();
		}
		if (group.getWinner().isPresent()) {
			Player winner = group.getWinner().get();
			if (winner.equals(group.getAway())) {
				away = "(WINNER) " + away;
			} else {
				home = home + " (WINNER)";
			}
		}
		IO.writeF("|%-44s|%44s|", home, away);
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
			
			
		if (!group.getNextRoundGroup().isPresent()) {
			Finish.finish();
			return;
		}
		
		Group nextGroup = group.getNextRoundGroup().get();
		if (group.getNextRoundSide().get() == 0) {
			nextGroup.setHome(player);
		} else {
			nextGroup.setAway(player);
		}
	}

}
