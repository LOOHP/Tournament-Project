package com.loohp.tournament.GUI;

import com.loohp.tournament.TournamentServer;

public class ConsoleTextOutput {
	
	public static void appendText(String string) {
		if (TournamentServer.GUIrunning) {
			GUI.textOutput.setText(GUI.textOutput.getText() + string);
			GUI.scrollPane.getVerticalScrollBar().setValue(GUI.scrollPane.getVerticalScrollBar().getMaximum());
		}
	}
	
	public static void appendText(String string, boolean isWriteLine) {
		if (isWriteLine) {
			appendText(string + "\n");
		} else {
			appendText(string);
		}
	}

}
