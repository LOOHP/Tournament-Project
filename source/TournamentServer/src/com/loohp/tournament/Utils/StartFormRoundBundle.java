package com.loohp.tournament.Utils;

import java.util.List;

import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Round.Round;

public class StartFormRoundBundle {
	
	List<Round> rounds;
	List<Group> groups;
	
	public StartFormRoundBundle(List<Round> rounds, List<Group> groups) {
		this.rounds = rounds;
		this.groups = groups;
	}
	
	public List<Round> getRounds() {
		return rounds;
	}
	
	public List<Group> getGroups() {
		return groups;
	}

}
