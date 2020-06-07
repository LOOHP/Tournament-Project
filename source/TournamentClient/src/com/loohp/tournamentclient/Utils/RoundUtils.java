package com.loohp.tournamentclient.Utils;

import com.loohp.tournamentclient.Lang;

public class RoundUtils {
	
	public static String getRoundNameFromPlayerCount(int playerCount) {
		String str = "";
		switch (playerCount) {
		case 2:
			str = "Final";
			if (Lang.lang.containsKey("Rounds.Final")) {
				str = Lang.lang.get("Rounds.Final").replace("%s", playerCount + "");
			}
			return str;
		case 4:
			str = "Semi-final";
			if (Lang.lang.containsKey("Rounds.SemiFinal")) {
				str = Lang.lang.get("Rounds.SemiFinal").replace("%s", playerCount + "");
			}
			return str;
		case 8:
			str = "Quater-final";
			if (Lang.lang.containsKey("Rounds.QuaterFinal")) {
				str = Lang.lang.get("Rounds.QuaterFinal").replace("%s", playerCount + "");
			}
			return str;
		default:
			str = "Round of " + playerCount;
			if (Lang.lang.containsKey("Rounds.Others")) {
				str = Lang.lang.get("Rounds.Others").replace("%s", playerCount + "");
			}
			return str;
		}
	}
	
	public static String getRoundNameFromRoundNumber(int roundNumber) {
		return getRoundNameFromPlayerCount((int) Math.pow(2, roundNumber + 1));
	}

}
