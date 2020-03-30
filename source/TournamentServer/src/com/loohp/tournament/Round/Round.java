package com.loohp.tournament.Round;

import java.util.List;

import com.loohp.tournament.Group.Group;
import com.loohp.tournament.Utils.RoundUtils;

public class Round {
	
	List<Group> groupList;
	int roundNum;
	
	public Round (List<Group> groupList, int roundNum) {
	    this.groupList = groupList;
	    this.roundNum = roundNum;
	}
	 
	public List<Group> getGroups() {
		return groupList;
	}
	 
	public void setGroups(List<Group> groupList) {
		this.groupList = groupList;
	}
	 
	public int getRoundNumber() {
		return roundNum;
	}
	 
	public void setRoundNumber(int roundNum) {
		this.roundNum = roundNum;
	}
	
	public String getName() {
		return RoundUtils.getRoundNameFromPlayerCount(groupList.size() * 2);
	}

}
