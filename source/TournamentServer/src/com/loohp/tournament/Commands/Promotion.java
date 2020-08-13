package com.loohp.tournament.Commands;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Utils.Finish;
import com.loohp.tournament.Utils.GroupUtils;
import com.loohp.tournament.Utils.IO;
import com.loohp.tournament.Utils.PlayerUtils;
import com.loohp.tournament.Utils.RoundCheck;

public class Promotion implements CommandExecutor {
	
	public void promote(String[] args) {
		
		if (!TournamentServer.getInstance().hasActiveCompetition()) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.CompetitionNotRunning"));
			return;
		}
		
		if (args.length < 2) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Promote.Usage"));
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
			IO.writeLn(TournamentServer.getInstance().getLang().get("Common.PlayerNotFound"));
			return;
		}
			
		Group group = PlayerUtils.getActiveRound(player, TournamentServer.getInstance().getActiveCompetition());
		
		if (group == null) {
			IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Promote.PlayerNotInAMatch"));
			return;
		}
		
		group.setWinner(Optional.of(player));
		IO.writeLn("===========================================================================================");
		String header = TournamentServer.getInstance().getLang().get("Commands.Promote.Header").replace("%s", GroupUtils.getMatchNumber(group) + "");
		IO.writeF("|%-89s|", header);
		IO.writeLn("");
		IO.writeLn("===========================================================================================");
		IO.writeF("|%-44s|%44s|", TournamentServer.getInstance().getLang().get("Common.Home"), TournamentServer.getInstance().getLang().get("Common.Away"));
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

	@Override
	public void execute(String[] args) {
		if (args[0].equalsIgnoreCase("promote")) {
			if (Arrays.asList(args).stream().anyMatch(each -> each.equalsIgnoreCase("--help"))) {
				IO.writeLn(TournamentServer.getInstance().getLang().get("Commands.Promote.Usage"));
			} else {
				promote(args);
				RoundCheck.check();
			}			
		}
	}

}
