package com.loohp.tournament.Group;

import java.util.Optional;

import com.loohp.tournament.Player.Player;
import com.loohp.tournament.Round.Round;

public class Group {
	
	Player home;
	Player away;
	Optional<Player> winner;
	Optional<Group> nextRoundGroup;
	Optional<Integer> nextRoundSide;
	Round round;
	
	public Group (Player home, Player away, Optional<Player> winner, Optional<Group> nextRoundGroup, Optional<Integer> nextRoundSide, Round round) {
	    this.home = home;
	    this.away = away;
	    this.nextRoundGroup = nextRoundGroup;
	    this.nextRoundSide = nextRoundSide;
	    this.winner = winner;
	    this.round = round;
	}
	 
	public Player getHome() {
		return home;
	}
	 
	public void setHome(Player home) {
		this.home = home;
	}
	 
	public Player getAway() {
		return away;
	}
	 
	public void setAway(Player away) {
		this.away = away;
	}
	 
	public Optional<Group> getNextRoundGroup() {
		return nextRoundGroup;
	}
	 
	public void setNextRoundGroup(Optional<Group> nextRoundGroup) {
		this.nextRoundGroup = nextRoundGroup;
	}
	 
	public Optional<Integer> getNextRoundSide() {
		return nextRoundSide;
	}
	 
	public void setNextRoundSide(Optional<Integer> nextRoundSide) {
		this.nextRoundSide = nextRoundSide;
	}
	
	public Optional<Player> getWinner() {
		return winner;
	}
	 
	public void setWinner(Optional<Player> winner) {
		this.winner = winner;
	}
	
	public Round getRound() {
		return round;
	}
	
	public void setRound(Round round) {
		this.round = round;
	}
}
