package com.loohp.tournamentclient.Utils;

import com.loohp.tournamentclient.TournamentClient;

public class TextOutputUtils {
	
	public static void appendText(String string) {
		if (TournamentClient.GUIrunning) {
			TournamentClient.getInstance().getTextOutput().setText(TournamentClient.getInstance().getTextOutput().getText() + string);
			TournamentClient.getInstance().getScrollPane().getVerticalScrollBar().setValue(TournamentClient.getInstance().getScrollPane().getVerticalScrollBar().getMaximum());
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
