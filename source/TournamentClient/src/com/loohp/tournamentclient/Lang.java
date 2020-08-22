package com.loohp.tournamentclient;

import java.awt.Window;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class Lang {
	
	public HashMap<String, String> lang = new HashMap<String, String>();
	
	public void load(HashMap<String, String> lang) {
		
		TournamentClient tour = TournamentClient.getInstance();
		
		if (lang.containsKey("Title")) {
			Window window = SwingUtilities.getWindowAncestor(tour.getHostLabel());
			JFrame frame = (JFrame) window;
			frame.setTitle(lang.get("Title").replace("%s", tour.getClient().host));
		}
		
		int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to load language pack provided by the server?");
		
		if(dialogResult == JOptionPane.YES_OPTION){
			
			if (lang.containsKey("HostLabel.Text")) {
				tour.getHostLabel().setText(lang.get("HostLabel.Text"));
			}
			if (lang.containsKey("HostLabel.ToolTip")) {
				tour.getHostLabel().setToolTipText(lang.get("HostLabel.ToolTip"));
			}
			
			if (lang.containsKey("HostText.Text")) {
				tour.getHostInput().setText(lang.get("HostText.Text"));
			}
			if (lang.containsKey("HostText.ToolTip")) {
				tour.getHostInput().setToolTipText(lang.get("HostText.ToolTip"));
			}
			
			if (lang.containsKey("ConnectButton.Text")) {
				tour.getHostConnect().setText(lang.get("ConnectButton.Text"));
			}
			if (lang.containsKey("ConnectButton.ToolTip")) {
				tour.getHostConnect().setToolTipText(lang.get("ConnectButton.ToolTip"));
			}
			
			if (lang.containsKey("ActionsLabel.Text")) {
				tour.getActionLabel().setText(lang.get("ActionsLabel.Text"));
			}
			if (lang.containsKey("ActionsLabel.ToolTip")) {
				tour.getActionLabel().setToolTipText(lang.get("ActionsLabel.ToolTip"));
			}
			
			if (lang.containsKey("PromoteButton.Text")) {
				tour.getPromoteButton().setText(lang.get("PromoteButton.Text"));
			}
			if (lang.containsKey("PromoteButton.ToolTip")) {
				tour.getPromoteButton().setToolTipText(lang.get("PromoteButton.ToolTip"));
			}
			
			if (lang.containsKey("PromoteText.Text")) {
				tour.getPromoteText().setText(lang.get("PromoteText.Text"));
			}
			if (lang.containsKey("PromoteText.ToolTip")) {
				tour.getPromoteText().setToolTipText(lang.get("PromoteText.ToolTip"));
			}
			
			if (lang.containsKey("DemoteButton.Text")) {
				tour.getDemotebutton().setText(lang.get("DemoteButton.Text"));
			}
			if (lang.containsKey("DemoteButton.ToolTip")) {
				tour.getDemotebutton().setToolTipText(lang.get("DemoteButton.ToolTip"));
			}
			
			if (lang.containsKey("DemoteText.Text")) {
				tour.getDemoteText().setText(lang.get("DemoteText.Text"));
			}
			if (lang.containsKey("DemoteText.ToolTip")) {
				tour.getDemoteText().setToolTipText(lang.get("DemoteText.ToolTip"));
			}
			
			if (lang.containsKey("FindPlayerButton.Text")) {
				tour.getFindPlayerButton().setText(lang.get("FindPlayerButton.Text"));
			}
			if (lang.containsKey("FindPlayerButton.ToolTip")) {
				tour.getFindPlayerButton().setToolTipText(lang.get("FindPlayerButton.ToolTip"));
			}
			
			if (lang.containsKey("FindPlayerText.Text")) {
				tour.getFindPlayerText().setText(lang.get("FindPlayerText.Text"));
			}
			if (lang.containsKey("FindPlayerText.ToolTip")) {
				tour.getFindPlayerText().setToolTipText(lang.get("FindPlayerText.ToolTip"));
			}
			
			if (lang.containsKey("ListRoundButton.Text")) {
				tour.getListRound().setText(lang.get("ListRoundButton.Text"));
			}
			if (lang.containsKey("ListRoundButton.ToolTip")) {
				tour.getListRound().setToolTipText(lang.get("ListRoundButton.ToolTip"));
			}
			
			if (lang.containsKey("ListCurrentRoundButton.Text")) {
				tour.getListCurrent().setText(lang.get("ListCurrentRoundButton.Text"));
			}
			if (lang.containsKey("ListCurrentRoundButton.ToolTip")) {
				tour.getListCurrent().setToolTipText(lang.get("ListCurrentRoundButton.ToolTip"));
			}
			
			if (lang.containsKey("ListPlayersButton.Text")) {
				tour.getListPlayers().setText(lang.get("ListPlayersButton.Text"));
			}
			if (lang.containsKey("ListPlayersButton.ToolTip")) {
				tour.getListPlayers().setToolTipText(lang.get("ListPlayersButton.ToolTip"));
			}
			
			if (lang.containsKey("ListUUIDButton.Text")) {
				tour.getListPlayerUUIDButton().setText(lang.get("ListUUIDButton.Text"));
			}
			if (lang.containsKey("ListUUIDButton.ToolTip")) {
				tour.getListPlayerUUIDButton().setToolTipText(lang.get("ListUUIDButton.ToolTip"));
			}
			
			if (lang.containsKey("GenerateReportButton.Text")) {
				tour.getGenReport().setText(lang.get("GenerateReportButton.Text"));
			}
			if (lang.containsKey("GenerateReportButton.ToolTip")) {
				tour.getGenReport().setToolTipText(lang.get("GenerateReportButton.ToolTip"));
			}
			
			if (lang.containsKey("ViewTournamentChartButton.Text")) {
				tour.getOpenTournyChart().setText(lang.get("ViewTournamentChartButton.Text"));
			}
			if (lang.containsKey("ViewTournamentChartButton.ToolTip")) {
				tour.getOpenTournyChart().setToolTipText(lang.get("ViewTournamentChartButton.ToolTip"));
			}
			
			if (lang.containsKey("OutputLabel.Text")) {
				tour.getConsoleLabel().setText(lang.get("OutputLabel.Text"));
			}
			if (lang.containsKey("OutputLabel.ToolTip")) {
				tour.getConsoleLabel().setToolTipText(lang.get("OutputLabel.ToolTip"));
			}
			
			if (lang.containsKey("CommandText.Text")) {
				tour.getCommandInput().setText(lang.get("CommandText.Text"));
			}
			if (lang.containsKey("CommandText.ToolTip")) {
				tour.getCommandInput().setToolTipText(lang.get("CommandText.ToolTip"));
			}		
			
			if (lang.containsKey("CommandButton.Text")) {
				tour.getExecCommand().setText(lang.get("CommandButton.Text"));
			}
			if (lang.containsKey("CommandButton.ToolTip")) {
				tour.getExecCommand().setToolTipText(lang.get("CommandButton.ToolTip"));
			}
		}
	}

}
