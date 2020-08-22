package com.loohp.tournamentclient.Utils;

import com.loohp.tournamentclient.TournamentClient;

public class RoundUtils {
	
	public static String getRoundNameFromPlayerCount(int playerCount) {
		String str = "";
		switch (playerCount) {
		case 2:
			str = "Final";
			if (TournamentClient.getInstance().getLang().lang.containsKey("Rounds.Final")) {
				str = TournamentClient.getInstance().getLang().lang.get("Rounds.Final").replace("%s", playerCount + "");
			}
			return str;
		case 4:
			str = "Semi-final";
			if (TournamentClient.getInstance().getLang().lang.containsKey("Rounds.SemiFinal")) {
				str = TournamentClient.getInstance().getLang().lang.get("Rounds.SemiFinal").replace("%s", playerCount + "");
			}
			return str;
		case 8:
			str = "Quater-final";
			if (TournamentClient.getInstance().getLang().lang.containsKey("Rounds.QuaterFinal")) {
				str = TournamentClient.getInstance().getLang().lang.get("Rounds.QuaterFinal").replace("%s", playerCount + "");
			}
			return str;
		default:
			str = "Round of " + playerCount;
			if (TournamentClient.getInstance().getLang().lang.containsKey("Rounds.Others")) {
				str = TournamentClient.getInstance().getLang().lang.get("Rounds.Others").replace("%s", playerCount + "");
			}
			return str;
		}
	}
	
	public static String getRoundNameFromRoundNumber(int roundNumber) {
		return getRoundNameFromPlayerCount((int) Math.pow(2, roundNumber + 1));
	}

}
