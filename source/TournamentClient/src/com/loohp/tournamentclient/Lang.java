package com.loohp.tournamentclient;

import java.awt.Window;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.loohp.tournamentclient.Utils.CustomHashMapUtils;

public class Lang {
	
	public static HashMap<String, String> lang = new HashMap<String, String>();
	
	@SuppressWarnings("unchecked")
	public static void load(String in) {
		
		String hash = in.substring(in.indexOf("function:serverlang=") + 20);
		
		if (((HashMap<String, String>) CustomHashMapUtils.deserialize(hash)).containsKey("Title")) {
			Window window = SwingUtilities.getWindowAncestor(TournamentClient.hostLabel);
			JFrame frame = (JFrame) window;
			frame.setTitle(((HashMap<String, String>) CustomHashMapUtils.deserialize(hash)).get("Title").replace("%s", Client.host));
		}
		
		int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to load language pack provided by the server?");
		
		if(dialogResult == JOptionPane.YES_OPTION){
		
			lang = (HashMap<String, String>) CustomHashMapUtils.deserialize(hash);
			
			if (lang.containsKey("HostLabel.Text")) {
				TournamentClient.hostLabel.setText(lang.get("HostLabel.Text"));
			}
			if (lang.containsKey("HostLabel.ToolTip")) {
				TournamentClient.hostLabel.setToolTipText(lang.get("HostLabel.ToolTip"));
			}
			
			if (lang.containsKey("HostText.Text")) {
				TournamentClient.hostInput.setText(lang.get("HostText.Text"));
			}
			if (lang.containsKey("HostText.ToolTip")) {
				TournamentClient.hostInput.setToolTipText(lang.get("HostText.ToolTip"));
			}
			
			if (lang.containsKey("ConnectButton.Text")) {
				TournamentClient.hostConnect.setText(lang.get("ConnectButton.Text"));
			}
			if (lang.containsKey("ConnectButton.ToolTip")) {
				TournamentClient.hostConnect.setToolTipText(lang.get("ConnectButton.ToolTip"));
			}
			
			if (lang.containsKey("ActionsLabel.Text")) {
				TournamentClient.actionLabel.setText(lang.get("ActionsLabel.Text"));
			}
			if (lang.containsKey("ActionsLabel.ToolTip")) {
				TournamentClient.actionLabel.setToolTipText(lang.get("ActionsLabel.ToolTip"));
			}
			
			if (lang.containsKey("PromoteButton.Text")) {
				TournamentClient.promoteButton.setText(lang.get("PromoteButton.Text"));
			}
			if (lang.containsKey("PromoteButton.ToolTip")) {
				TournamentClient.promoteButton.setToolTipText(lang.get("PromoteButton.ToolTip"));
			}
			
			if (lang.containsKey("PromoteText.Text")) {
				TournamentClient.promoteText.setText(lang.get("PromoteText.Text"));
			}
			if (lang.containsKey("PromoteText.ToolTip")) {
				TournamentClient.promoteText.setToolTipText(lang.get("PromoteText.ToolTip"));
			}
			
			if (lang.containsKey("DemoteButton.Text")) {
				TournamentClient.demotebutton.setText(lang.get("DemoteButton.Text"));
			}
			if (lang.containsKey("DemoteButton.ToolTip")) {
				TournamentClient.demotebutton.setToolTipText(lang.get("DemoteButton.ToolTip"));
			}
			
			if (lang.containsKey("DemoteText.Text")) {
				TournamentClient.demoteText.setText(lang.get("DemoteText.Text"));
			}
			if (lang.containsKey("DemoteText.ToolTip")) {
				TournamentClient.demoteText.setToolTipText(lang.get("DemoteText.ToolTip"));
			}
			
			if (lang.containsKey("FindPlayerButton.Text")) {
				TournamentClient.findPlayerButton.setText(lang.get("FindPlayerButton.Text"));
			}
			if (lang.containsKey("FindPlayerButton.ToolTip")) {
				TournamentClient.findPlayerButton.setToolTipText(lang.get("FindPlayerButton.ToolTip"));
			}
			
			if (lang.containsKey("FindPlayerText.Text")) {
				TournamentClient.findPlayerText.setText(lang.get("FindPlayerText.Text"));
			}
			if (lang.containsKey("FindPlayerText.ToolTip")) {
				TournamentClient.findPlayerText.setToolTipText(lang.get("FindPlayerText.ToolTip"));
			}
			
			if (lang.containsKey("ListRoundButton.Text")) {
				TournamentClient.listRound.setText(lang.get("ListRoundButton.Text"));
			}
			if (lang.containsKey("ListRoundButton.ToolTip")) {
				TournamentClient.listRound.setToolTipText(lang.get("ListRoundButton.ToolTip"));
			}
			
			if (lang.containsKey("ListCurrentRoundButton.Text")) {
				TournamentClient.listCurrent.setText(lang.get("ListCurrentRoundButton.Text"));
			}
			if (lang.containsKey("ListCurrentRoundButton.ToolTip")) {
				TournamentClient.listCurrent.setToolTipText(lang.get("ListCurrentRoundButton.ToolTip"));
			}
			
			if (lang.containsKey("ListPlayersButton.Text")) {
				TournamentClient.listPlayers.setText(lang.get("ListPlayersButton.Text"));
			}
			if (lang.containsKey("ListPlayersButton.ToolTip")) {
				TournamentClient.listPlayers.setToolTipText(lang.get("ListPlayersButton.ToolTip"));
			}
			
			if (lang.containsKey("ListUUIDButton.Text")) {
				TournamentClient.listPlayerUUIDButton.setText(lang.get("ListUUIDButton.Text"));
			}
			if (lang.containsKey("ListUUIDButton.ToolTip")) {
				TournamentClient.listPlayerUUIDButton.setToolTipText(lang.get("ListUUIDButton.ToolTip"));
			}
			
			if (lang.containsKey("GenerateReportButton.Text")) {
				TournamentClient.genReport.setText(lang.get("GenerateReportButton.Text"));
			}
			if (lang.containsKey("GenerateReportButton.ToolTip")) {
				TournamentClient.genReport.setToolTipText(lang.get("GenerateReportButton.ToolTip"));
			}
			
			if (lang.containsKey("ViewTournamentChartButton.Text")) {
				TournamentClient.openTournyChart.setText(lang.get("ViewTournamentChartButton.Text"));
			}
			if (lang.containsKey("ViewTournamentChartButton.ToolTip")) {
				TournamentClient.openTournyChart.setToolTipText(lang.get("ViewTournamentChartButton.ToolTip"));
			}
			
			if (lang.containsKey("OutputLabel.Text")) {
				TournamentClient.consoleLabel.setText(lang.get("OutputLabel.Text"));
			}
			if (lang.containsKey("OutputLabel.ToolTip")) {
				TournamentClient.consoleLabel.setToolTipText(lang.get("OutputLabel.ToolTip"));
			}
			
			if (lang.containsKey("CommandText.Text")) {
				TournamentClient.commandInput.setText(lang.get("CommandText.Text"));
			}
			if (lang.containsKey("CommandText.ToolTip")) {
				TournamentClient.commandInput.setToolTipText(lang.get("CommandText.ToolTip"));
			}		
			
			if (lang.containsKey("CommandButton.Text")) {
				TournamentClient.execCommand.setText(lang.get("CommandButton.Text"));
			}
			if (lang.containsKey("CommandButton.ToolTip")) {
				TournamentClient.execCommand.setToolTipText(lang.get("CommandButton.ToolTip"));
			}
		}
	}

}
