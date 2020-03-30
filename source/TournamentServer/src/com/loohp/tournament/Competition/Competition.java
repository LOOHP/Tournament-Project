package com.loohp.tournament.Competition;

import java.util.List;

import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Round.Round;

public class Competition {
	
	List<Round> roundList;
	List<Group> groupList;
	int activeRound;
	
	public Competition (List<Round> roundList, List<Group> groupList, int activeRound) {
	    this.roundList = roundList;
	    this.groupList = groupList;
	    this.activeRound = activeRound;
	}
	 
	public List<Round> getRounds() {
		return roundList;
	}
	 
	public void setRounds(List<Round> roundList) {
		this.roundList = roundList;
	}
	
	public List<Group> getGroups() {
		return groupList;
	}
	 
	public void setGroups(List<Group> groupList) {
		this.groupList = groupList;
	}
	
	public int getActiveRound() {
		return activeRound;
	}
	 
	public void setActiveRound(int activeRound) {
		this.activeRound = activeRound;
	}
}
