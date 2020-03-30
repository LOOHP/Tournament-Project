package com.loohp.tournament.GUI;

import com.loohp.tournament.Tournament;

public class ClientTextOutput {
	
	public static void setText(String string) {
		if (Tournament.GUIrunning) {
			GUI.clientText.setText(string);
		}
	}

}
