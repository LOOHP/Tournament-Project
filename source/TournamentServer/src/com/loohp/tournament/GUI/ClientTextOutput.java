package com.loohp.tournament.GUI;

import com.loohp.tournament.TournamentServer;

public class ClientTextOutput {
	
	public static void setText(String string) {
		if (TournamentServer.getInstance().isGUIrunning()) {
			GUI.clientText.setText(string);
		}
	}

}
