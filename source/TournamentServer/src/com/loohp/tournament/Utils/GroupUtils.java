package com.loohp.tournament.Utils;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Group.Group;

public class GroupUtils {
	public static int getMatchNumber(Group group) {
		return TournamentServer.getInstance().getActiveCompetition().getGroups().indexOf(group) + 1;
	}
	
	public static Group getHomeSideLastGroup(Group group) {
		for (Group each : TournamentServer.getInstance().getActiveCompetition().getGroups()) {
			if (each.getNextRoundGroup().isPresent()) {
				if (each.getNextRoundGroup().get().equals(group)) {
					if (each.getNextRoundSide().get() == 0) {
						return each;
					}
				}
			}
		}
		return null;
	}
	
	public static Group getAwaySideLastGroup(Group group) {
		for (Group each : TournamentServer.getInstance().getActiveCompetition().getGroups()) {
			if (each.getNextRoundGroup().isPresent()) {
				if (each.getNextRoundGroup().get().equals(group)) {
					if (each.getNextRoundSide().get() == 1) {
						return each;
					}
				}
			}
		}
		return null;
	}
	
}
