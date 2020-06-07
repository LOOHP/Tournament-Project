package com.loohp.tournamentclient.Utils;

import com.loohp.tournamentclient.TournamentClient;

public class TextOutputUtils {
	
	public static void appendText(String string) {
		if (TournamentClient.GUIrunning) {
			TournamentClient.textOutput.setText(TournamentClient.textOutput.getText() + string);
			TournamentClient.scrollPane.getVerticalScrollBar().setValue(TournamentClient.scrollPane.getVerticalScrollBar().getMaximum());
		}
		System.out.print(string);
	}
	
	public static void appendText(String string, boolean isWriteLine) {
		if (isWriteLine) {
			appendText(string + "\n");
		} else {
			appendText(string);
		}
	}

}
