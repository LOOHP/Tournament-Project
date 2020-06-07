package com.loohp.tournament.Utils;

import java.util.Optional;

import com.loohp.tournament.Lang;
import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;

public class RoundCheck {
	
	public static void check() {
		
		if (!TournamentServer.activeCompetition.isPresent()) {
			return;
		}
		
		boolean moveToNextRound = true;
		
		for (Group group : TournamentServer.activeCompetition.get().getRounds().get(TournamentServer.activeCompetition.get().getActiveRound()).getGroups()) {
			Player home = group.getHome();
			Player away = group.getAway();
			if (home.getName().equals("_BYE_") && !away.getName().equals("_BYE_") ) {
				group.setWinner(Optional.of(away));
				if (group.getNextRoundGroup().isPresent()) {
					if (group.getNextRoundSide().get() == 0) {
						group.getNextRoundGroup().get().setHome(away);
					} else {
						group.getNextRoundGroup().get().setAway(away);
					}
				}
			} else if (away.getName().equals("_BYE_") && !home.getName().equals("_BYE_") ) {
				group.setWinner(Optional.of(home));
				if (group.getNextRoundGroup().isPresent()) {
					if (group.getNextRoundSide().get() == 0) {
						group.getNextRoundGroup().get().setHome(home);
					} else {
						group.getNextRoundGroup().get().setAway(home);
					}
				}
			}			
			
			if (!group.getWinner().isPresent()) {
				moveToNextRound = false;
			}
		}		
		
		if (moveToNextRound) {
			String roundName = TournamentServer.activeCompetition.get().getRounds().get(TournamentServer.activeCompetition.get().getActiveRound()).getName();
			IO.writeLn(Lang.getLang("Functions.RoundCheck.Complete").replace("%s", roundName));
			if (TournamentServer.activeCompetition.get().getRounds().size() > TournamentServer.activeCompetition.get().getActiveRound()) {
				TournamentServer.activeCompetition.get().setActiveRound(TournamentServer.activeCompetition.get().getActiveRound() + 1);
				String nextRoundName = TournamentServer.activeCompetition.get().getRounds().get(TournamentServer.activeCompetition.get().getActiveRound()).getName();
				IO.writeLn(Lang.getLang("Functions.RoundCheck.BeginNext").replace("%s", nextRoundName));
			}
		}
	}

}
