
package com.loohp.tournament.Commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import com.loohp.tournament.Lang;
import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.Competition.Competition;
import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;
import com.loohp.tournament.Utils.IO;

public class Start {
	
	public static void startCompetition() {
		
		if (TournamentServer.activeCompetition.isPresent()) {
			IO.writeLn(Lang.getLang("Common.CompetitionAlreadyRunning"));
			return;
		}
		
		if (TournamentServer.playerList.size() <= 1) {
			IO.writeLn(Lang.getLang("Commands.Start.NotEnoughPlayers"));
			return;
		} else if (TournamentServer.playerList.size() <= 2) {
			IO.writeLn(Lang.getLang("Commands.Start.Begin"));
			long start = System.currentTimeMillis();
			
			List<Group> groups = new ArrayList<Group>();
			Group group = new Group(TournamentServer.playerList.get(0), TournamentServer.playerList.get(1), Optional.empty(), Optional.empty(), Optional.empty(), null);
			groups.add(group);
			
			List<Round> rounds = new ArrayList<Round>();
			Round round = new Round(groups, 0);
			rounds.add(round);
			
			group.setRound(round);
			
			Competition comp = formCompetition(rounds, groups);
			TournamentServer.activeCompetition = Optional.of(comp);
			
			long end = System.currentTimeMillis();
			IO.writeLn(Lang.getLang("Commands.Start.Done").replace("%s", "(" + (end - start) + "ms)"));
			return;
		}
		
		IO.writeLn(Lang.getLang("Commands.Start.Begin"));
		long start = System.currentTimeMillis();
		
		List<Player> players = new ArrayList<Player>(TournamentServer.playerList);
		long total = 0;
		int roundNum = 0;
		for (int i = 1; total < players.size(); i++) {
			total = (int) Math.pow(2, i);
			roundNum = i;
		}
		while (players.size() < total) {
			players.add(new Player(UUID.randomUUID(), "_BYE_", "N/A", false));
		}
		
		Collections.shuffle(players);
		List<List<Player>> pairs = formPair(players);
		
		for (List<Player> each : pairs) {
			if (each.get(0).getName().equals("_BYE_")) {
				Player top = each.get(0);
				each.set(0, each.get(1));
				each.set(1, top);
				continue;
			}
			if (each.get(1).getSeeded()) {
				if (!each.get(0).getSeeded()) {
					Player top = each.get(0);
					each.set(0, each.get(1));
					each.set(1, top);
					continue;
				}
			}
			if (each.get(0).getSeeded()) {
				if (!each.get(1).getSeeded()) {
					continue;
				}
			}
			int ran = (int) (Math.random() * 100);
			if (ran < 50) {
				Player top = each.get(0);
				each.set(0, each.get(1));
				each.set(1, top);
			}
		}
		
		List<Object> obj = formRounds(pairs, roundNum);
		@SuppressWarnings("unchecked")
		List<Round> rounds = (List<Round>) obj.get(0);
		@SuppressWarnings("unchecked")
		List<Group> groups = (List<Group>) obj.get(1);
		Competition comp = formCompetition(rounds, groups);
		TournamentServer.activeCompetition = Optional.of(comp);
		long end = System.currentTimeMillis();
		
		IO.writeLn(Lang.getLang("Commands.Start.Done").replace("%s", "(" + (end - start) + "ms)"));
	}
	
	public static Competition formCompetition(List<Round> round, List<Group> group) {
		Competition comp = new Competition(round, group, 0);
		return comp;
	}
	
	public static List<Object> formRounds(List<List<Player>> players, int rounds) {
		List<Group> list = new ArrayList<Group>();
		List<Round> roundList = new ArrayList<Round>();
		
		Optional<Group> parent = Optional.empty();
		for (int i = 1; i <= rounds; i++) {
			List<Group> thisRound = new ArrayList<Group>();
			Round round = new Round(thisRound, i - 1);
			double offset = Math.pow(2, i - 2) - 1;
			for (int u = 1; u <= Math.pow(2, i - 1); u++) {
				if (parent.isPresent()) {				
					parent = Optional.of(list.get(list.indexOf(parent.get()) - (int) Math.ceil(offset)));
				}
				Group group = new Group(null, null, Optional.empty(), parent, Optional.empty(), round);
				parent = Optional.of(group);
				thisRound.add(group);
				list.add(group);
				offset = offset + 0.5;
			}
			Collections.reverse(thisRound);
			roundList.add(round);
		}
		Collections.reverse(list);
		Collections.reverse(roundList);
		
		for (int i = 0; i < players.size(); i++) {
			List<Player> pair = players.get(i);
			Player home = pair.get(0);
			Player away = pair.get(1);
			Group group = list.get(i);
			group.setHome(home);
			group.setAway(away);
			list.set(i, group);
		}
		
		int num = 0;
		for (int i = 0; i < list.size() - 1; i++) {
			list.get(i).setNextRoundSide(Optional.of(num));
			if (num == 1) {
				num = 0;
			} else {
				num = 1;
			}
		}
		
		List<Object> returnlist = new ArrayList<Object>();
		returnlist.add(roundList);
		returnlist.add(list);
		return returnlist;
	}
	
	public static List<List<Player>> formPair(List<Player> players) {
		List<List<Player>> list = new ArrayList<List<Player>>();
		
		List<Player> playersPod = new CopyOnWriteArrayList<Player>(players);
		
		List<List<Player>> retu = split(playersPod);
		for (List<Player> each : retu) {
			if (each.size() > 2) {
				list.addAll(formPair(each));
			} else {
				list.add(each);
			}
		}
		return list;
	}
	
	public static List<List<Player>> split(List<Player> players) {
		
		List<Player> playersPod = new ArrayList<Player>(players);
		int max = players.size() / 2;
		
		List<Player> group1 = new ArrayList<Player>();
		List<Player> group2 = new ArrayList<Player>();
		
		Iterator<Player> itr = playersPod.iterator();
		
		while (itr.hasNext()) {
			Player player = itr.next();
			if (player.getName().equals("_BYE_")) {
				group1.add(player);
				itr.remove();
			} else {
				continue;
			}
			
			while (itr.hasNext()) {
				player = itr.next();
				if (player.getName().equals("_BYE_")) {
					group2.add(player);
					itr.remove();
					break;
				} else {
					continue;
				}
			}
		}

		itr = playersPod.iterator();

		while (itr.hasNext()) {
			Player player = itr.next();
			if (group1.size() == max) {
				group2.add(player);
				itr.remove();
				continue;
			} else if (group2.size() == max) {
				group1.add(player);
				itr.remove();
				continue;
			}
			if (player.getSeeded()) {
				group1.add(player);
				itr.remove();
			} else {
				continue;
			}
			
			while (itr.hasNext()) {
				player = itr.next();
				if (player.getSeeded()) {
					group2.add(player);
					itr.remove();
					break;
				} else {
					continue;
				}
			}
		}

		itr = playersPod.iterator();
		
		while (itr.hasNext()) {
			Player player = itr.next();
			if (group1.size() == max) {
				group2.add(player);
				itr.remove();
				continue;
			} else if (group2.size() == max) {
				group1.add(player);
				itr.remove();
				continue;
			}
			String school = player.getSchool();
			long count1 = 0;
			long count2 = 0;
			for (Player each : group1) {
				if (each.getSchool().equals(school)) {
					count1++;
				}
			}
			for (Player each : group2) {
				if (each.getSchool().equals(school)) {
					count2++;
				}
			}
			if (count1 <= count2) {
				group1.add(player);
				itr.remove();
				if (itr.hasNext()) {
					group2.add(itr.next());
					itr.remove();				
				}			
			} else {
				group2.add(player);
				itr.remove();
				if (itr.hasNext()) {
					group1.add(itr.next());
					itr.remove();				
				}	
			}
		}	
		
		List<List<Player>> list = new ArrayList<List<Player>>();
		list.add(group1);
		list.add(group2);
		return list;
	}

}
