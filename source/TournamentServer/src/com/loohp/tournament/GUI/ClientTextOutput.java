package com.loohp.tournament.GUI;

import com.loohp.tournament.TournamentServer;

public class ClientTextOutput {
	
	public static void setText(String string) {
		if (TournamentServer.GUIrunning) {
			GUI.clientText.setText(string);
		}
	}

}
