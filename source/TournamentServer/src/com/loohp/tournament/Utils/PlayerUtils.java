package com.loohp.tournament.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;

public class PlayerUtils {

	public static List<Group> getGroups(Player player, Competition comp) {
		List<Group> list = new ArrayList<Group>();
		for (Group group : comp.getGroups()) {
			if (group.getHome() != null) {
				if (group.getHome().equals(player)) {
					list.add(group);
				}
			}
			if (group.getAway() != null) {
				if (group.getAway().equals(player)) {
					list.add(group);
				}
			}
		}
		return list;
	}
	
	public static Group getGroup(Player player, Competition comp, int roundNum) {
		for (Group group : comp.getRounds().get(roundNum).getGroups()) {
			if (group.getHome().equals(player)) {
				return group;
			} else if (group.getAway().equals(player)) {
				return group;
			}
		}
		return null;
	}
	
	public static Group getActiveRound(Player player, Competition comp) {
		return getGroup(player, comp, comp.getActiveRound());
	}
	
	public static Player getPlayer(String name) {
		for (Player player : TournamentServer.playerList) {
			if (player.getName().equalsIgnoreCase(name)) {
				return player;
			}
		}
		return null;
	}
	
	public static Player getPlayer(UUID uuid) {
		for (Player player : TournamentServer.playerList) {
			if (player.getId().equals(uuid)) {
				return player;
			}
		}
		return null;
	}
}
