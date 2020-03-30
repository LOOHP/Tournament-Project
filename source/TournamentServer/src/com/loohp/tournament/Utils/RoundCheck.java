package com.loohp.tournament.Utils;

import java.util.Optional;

import com.loohp.tournament.Lang;
import com.loohp.tournament.Tournament;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;

public class RoundCheck {
	
	public static void check() {
		
		if (!Tournament.activeCompetition.isPresent()) {
			return;
		}
		
		boolean moveToNextRound = true;
		
		for (Group group : Tournament.activeCompetition.get().getRounds().get(Tournament.activeCompetition.get().getActiveRound()).getGroups()) {
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
			String roundName = Tournament.activeCompetition.get().getRounds().get(Tournament.activeCompetition.get().getActiveRound()).getName();
			IO.writeLn(Lang.getLang("Functions.RoundCheck.Complete").replace("%s", roundName));
			if (Tournament.activeCompetition.get().getRounds().size() > Tournament.activeCompetition.get().getActiveRound()) {
				Tournament.activeCompetition.get().setActiveRound(Tournament.activeCompetition.get().getActiveRound() + 1);
				String nextRoundName = Tournament.activeCompetition.get().getRounds().get(Tournament.activeCompetition.get().getActiveRound()).getName();
				IO.writeLn(Lang.getLang("Functions.RoundCheck.BeginNext").replace("%s", nextRoundName));
			}
		}
	}

}
