package com.loohp.tournamentclient;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import com.loohp.tournamentclient.ComboBox.ListDropDownRound;
import com.loohp.tournamentclient.Utils.TextOutputUtils;

@SuppressWarnings("serial")
public class TournamentClient extends JFrame {

	public static JPanel contentPane;
	public static JTextField hostInput;
	public static JTextField commandInput;
	public static JButton execCommand;
	public static JTextPane textOutput;
	public static JButton hostConnect;
	public static JLabel hostLabel;
	public static JScrollPane scrollPane;
	public static JLabel consoleLabel;
	public static JButton openTournyChart;
	public static JButton genReport;
	public static JLabel actionLabel;
	public static JButton listPlayers;
	public static JButton listCurrent;
	public static JButton listRound;
	public static JComboBox<ListDropDownRound> listDropDownBox;
	public static JButton demotebutton;
	public static JButton promoteButton;
	public static JTextField promoteText;
	public static JTextField demoteText;
	public static JButton listPlayerUUIDButton;
	public static JButton findPlayerButton;
	public static JTextField findPlayerText;
	
	public static List<String> history = new ArrayList<String>();
	public static int currenthistory = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TournamentClient frame = new TournamentClient();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TournamentClient() {
		setTitle("Tournament System");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1198, 686);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{134, 0, 146, 36, 0, 676, 111, 0};
		gbl_contentPane.rowHeights = new int[]{37, 0, 33, 10, 33, 66, 33, 10, 33, 10, 33, 10, 33, 10, 33, 10, 33, 35, 10, 33, 0};
		gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		hostLabel = new JLabel("Host");
		hostLabel.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_hostLabel = new GridBagConstraints();
		gbc_hostLabel.fill = GridBagConstraints.VERTICAL;
		gbc_hostLabel.insets = new Insets(0, 0, 5, 5);
		gbc_hostLabel.gridx = 0;
		gbc_hostLabel.gridy = 0;
		contentPane.add(hostLabel, gbc_hostLabel);
		
		hostInput = new JTextField();
		hostInput.setToolTipText("Input the host (and the port) of the server");
		hostInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					String host = hostInput.getText();
					int port = 1720;
					if (hostInput.getText().contains(":")) {
						host = hostInput.getText().substring(0, hostInput.getText().indexOf(":"));
						port = Integer.parseInt(hostInput.getText().substring(hostInput.getText().indexOf(":") + 1));
					}
					Client.connect(host, port);
				}
			}
		});
		hostInput.setFont(new Font("Tahoma", Font.PLAIN, 19));
		hostInput.setText("localhost:1720");
		GridBagConstraints gbc_hostInput = new GridBagConstraints();
		gbc_hostInput.gridwidth = 4;
		gbc_hostInput.insets = new Insets(0, 0, 5, 5);
		gbc_hostInput.fill = GridBagConstraints.BOTH;
		gbc_hostInput.gridx = 2;
		gbc_hostInput.gridy = 0;
		contentPane.add(hostInput, gbc_hostInput);
		hostInput.setColumns(10);
		
		hostConnect = new JButton("Connect");
		hostConnect.setToolTipText("Connects to a tournament server");
		hostConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (hostInput.getText().equals("")) {
					return;
				}
				String host = hostInput.getText();
				int port = 1720;
				if (hostInput.getText().contains(":")) {
					host = hostInput.getText().substring(0, hostInput.getText().indexOf(":"));
					port = Integer.parseInt(hostInput.getText().substring(hostInput.getText().indexOf(":") + 1));
				}
				Client.connect(host, port);
			}
		});
		hostConnect.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_hostConnect = new GridBagConstraints();
		gbc_hostConnect.fill = GridBagConstraints.BOTH;
		gbc_hostConnect.insets = new Insets(0, 0, 5, 0);
		gbc_hostConnect.gridx = 6;
		gbc_hostConnect.gridy = 0;
		contentPane.add(hostConnect, gbc_hostConnect);
		
		actionLabel = new JLabel("Actions");
		actionLabel.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_actionLabel = new GridBagConstraints();
		gbc_actionLabel.anchor = GridBagConstraints.WEST;
		gbc_actionLabel.insets = new Insets(0, 0, 5, 5);
		gbc_actionLabel.gridx = 0;
		gbc_actionLabel.gridy = 1;
		contentPane.add(actionLabel, gbc_actionLabel);
		
		consoleLabel = new JLabel("Output");
		consoleLabel.setFont(new Font("Arial", Font.BOLD, 11));
		GridBagConstraints gbc_consoleLabel = new GridBagConstraints();
		gbc_consoleLabel.anchor = GridBagConstraints.WEST;
		gbc_consoleLabel.insets = new Insets(0, 0, 5, 5);
		gbc_consoleLabel.gridx = 5;
		gbc_consoleLabel.gridy = 1;
		contentPane.add(consoleLabel, gbc_consoleLabel);
		
		commandInput = new JTextField();
		commandInput.setToolTipText("Input a command");
		commandInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (commandInput.getText().equals("")) {
					return;
				}
				if (e.getKeyCode() == 10) {
					String cmd = commandInput.getText();
					history.add(cmd);
					currenthistory = history.size();
					Client.send(cmd);
					commandInput.setText("");
				} else if (e.getKeyCode() == 38) {
					currenthistory--;
					if (currenthistory >= 0) {
						commandInput.setText(history.get(currenthistory));
					} else {
						currenthistory++;
					}
				} else if (e.getKeyCode() == 40) {
					currenthistory++;
					if (currenthistory < history.size()) {
						commandInput.setText(history.get(currenthistory));
					} else {
						currenthistory--;
					}
				}
			}
		});
		
		listRound = new JButton("List Round");
		listRound.setToolTipText("List information about the chosen round");
		listRound.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				int round = ((ListDropDownRound) listDropDownBox.getSelectedItem()).getId();
				String cmd = "list round " + round;
				Client.send(cmd);
			}
		});
		
		promoteButton = new JButton("Promote");
		promoteButton.setToolTipText("Promotes a player to the next stage");
		promoteButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (promoteText.getText().equals("")) {
					return;
				}
				String cmd = "promote " + promoteText.getText();
				Client.send(cmd);
				promoteText.setText("");
			}
		});
		promoteButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_promoteButton = new GridBagConstraints();
		gbc_promoteButton.fill = GridBagConstraints.BOTH;
		gbc_promoteButton.insets = new Insets(0, 0, 5, 5);
		gbc_promoteButton.gridx = 0;
		gbc_promoteButton.gridy = 2;
		contentPane.add(promoteButton, gbc_promoteButton);
		
		promoteText = new JTextField();
		promoteText.setToolTipText("Enter a player name or a UUID");
		promoteText.setFont(new Font("Tahoma", Font.PLAIN, 19));
		promoteText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (promoteText.getText().equals("")) {
					return;
				}
				if (e.getKeyCode() == 10) {
					String cmd = "promote " + promoteText.getText();
					Client.send(cmd);
					promoteText.setText("");
				}
			}
		});
		GridBagConstraints gbc_promoteText = new GridBagConstraints();
		gbc_promoteText.gridwidth = 3;
		gbc_promoteText.insets = new Insets(0, 0, 5, 5);
		gbc_promoteText.fill = GridBagConstraints.BOTH;
		gbc_promoteText.gridx = 1;
		gbc_promoteText.gridy = 2;
		contentPane.add(promoteText, gbc_promoteText);
		promoteText.setColumns(10);
		
		demotebutton = new JButton("Demote");
		demotebutton.setToolTipText("Undo a promotion in current stage");
		demotebutton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (demoteText.getText().equals("")) {
					return;
				}
				String cmd = "unpromote " + demoteText.getText();
				Client.send(cmd);
				demoteText.setText("");
			}
		});
		demotebutton.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_demotebutton = new GridBagConstraints();
		gbc_demotebutton.fill = GridBagConstraints.BOTH;
		gbc_demotebutton.insets = new Insets(0, 0, 5, 5);
		gbc_demotebutton.gridx = 0;
		gbc_demotebutton.gridy = 4;
		contentPane.add(demotebutton, gbc_demotebutton);
		
		demoteText = new JTextField();
		demoteText.setToolTipText("Enter a player name or a UUID");
		demoteText.setFont(new Font("Tahoma", Font.PLAIN, 19));
		demoteText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (demoteText.getText().equals("")) {
					return;
				}
				if (e.getKeyCode() == 10) {
					String cmd = "unpromote " + demoteText.getText();
					Client.send(cmd);
					demoteText.setText("");
				}
			}
		});
		GridBagConstraints gbc_demoteText = new GridBagConstraints();
		gbc_demoteText.gridwidth = 3;
		gbc_demoteText.insets = new Insets(0, 0, 5, 5);
		gbc_demoteText.fill = GridBagConstraints.BOTH;
		gbc_demoteText.gridx = 1;
		gbc_demoteText.gridy = 4;
		contentPane.add(demoteText, gbc_demoteText);
		demoteText.setColumns(10);
		
		findPlayerButton = new JButton("Find Player");
		findPlayerButton.setToolTipText("Search for a player");
		findPlayerButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (findPlayerText.getText().equals("")) {
					return;
				}
				String cmd = "find " + findPlayerText.getText();
				Client.send(cmd);
				findPlayerText.setText("");
			}
		});
		findPlayerButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_findPlayerButton = new GridBagConstraints();
		gbc_findPlayerButton.fill = GridBagConstraints.BOTH;
		gbc_findPlayerButton.insets = new Insets(0, 0, 5, 5);
		gbc_findPlayerButton.gridx = 0;
		gbc_findPlayerButton.gridy = 6;
		contentPane.add(findPlayerButton, gbc_findPlayerButton);
		
		findPlayerText = new JTextField();
		findPlayerText.setToolTipText("Enter a player name or a UUID");
		findPlayerText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (findPlayerText.getText().equals("")) {
					return;
				}
				if (e.getKeyCode() == 10) {
					String cmd = "find " + findPlayerText.getText();
					Client.send(cmd);
					findPlayerText.setText("");
				}
			}
		});
		findPlayerText.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_findPlayerText = new GridBagConstraints();
		gbc_findPlayerText.gridwidth = 3;
		gbc_findPlayerText.insets = new Insets(0, 0, 5, 5);
		gbc_findPlayerText.fill = GridBagConstraints.BOTH;
		gbc_findPlayerText.gridx = 1;
		gbc_findPlayerText.gridy = 6;
		contentPane.add(findPlayerText, gbc_findPlayerText);
		findPlayerText.setColumns(10);
		listRound.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_listRound = new GridBagConstraints();
		gbc_listRound.fill = GridBagConstraints.BOTH;
		gbc_listRound.insets = new Insets(0, 0, 5, 5);
		gbc_listRound.gridx = 0;
		gbc_listRound.gridy = 8;
		contentPane.add(listRound, gbc_listRound);
		
		listDropDownBox = new JComboBox<>();
		listDropDownBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				Client.send("function:getroundnum");
			}
		});
		GridBagConstraints gbc_listDropDownBox = new GridBagConstraints();
		gbc_listDropDownBox.fill = GridBagConstraints.BOTH;
		gbc_listDropDownBox.gridwidth = 3;
		gbc_listDropDownBox.insets = new Insets(0, 0, 5, 5);
		gbc_listDropDownBox.gridx = 1;
		gbc_listDropDownBox.gridy = 8;
		contentPane.add(listDropDownBox, gbc_listDropDownBox);
		
		listCurrent = new JButton("List Current Round");
		listCurrent.setToolTipText("List information about the current round");
		listCurrent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Client.send("list current");
			}
		});
		listCurrent.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_listCurrent = new GridBagConstraints();
		gbc_listCurrent.fill = GridBagConstraints.BOTH;
		gbc_listCurrent.gridwidth = 4;
		gbc_listCurrent.insets = new Insets(0, 0, 5, 5);
		gbc_listCurrent.gridx = 0;
		gbc_listCurrent.gridy = 10;
		contentPane.add(listCurrent, gbc_listCurrent);
		
		listPlayers = new JButton("List Players");
		listPlayers.setToolTipText("List all players");
		listPlayers.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Client.send("list players");
			}
		});
		listPlayers.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_listPlayers = new GridBagConstraints();
		gbc_listPlayers.gridwidth = 3;
		gbc_listPlayers.fill = GridBagConstraints.BOTH;
		gbc_listPlayers.insets = new Insets(0, 0, 5, 5);
		gbc_listPlayers.gridx = 0;
		gbc_listPlayers.gridy = 12;
		contentPane.add(listPlayers, gbc_listPlayers);
		
		scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridheight = 16;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 5;
		gbc_scrollPane.gridy = 2;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		textOutput = new JTextPane();
		scrollPane.setViewportView(textOutput);
		textOutput.setFont(new Font("Consolas", Font.PLAIN, 12));
		textOutput.setEditable(false);
		
		genReport = new JButton("Generate Report");
		genReport.setToolTipText("Generate and download a report");
		genReport.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Client.send("function:getreport");
			}
		});
		
		listPlayerUUIDButton = new JButton("UUID");
		listPlayerUUIDButton.setToolTipText("List players' UUID");
		listPlayerUUIDButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				Client.send("list players --uuid");
			}
		});
		listPlayerUUIDButton.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_listPlayerUUIDButton = new GridBagConstraints();
		gbc_listPlayerUUIDButton.fill = GridBagConstraints.BOTH;
		gbc_listPlayerUUIDButton.insets = new Insets(0, 0, 5, 5);
		gbc_listPlayerUUIDButton.gridx = 3;
		gbc_listPlayerUUIDButton.gridy = 12;
		contentPane.add(listPlayerUUIDButton, gbc_listPlayerUUIDButton);
		genReport.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_genReport = new GridBagConstraints();
		gbc_genReport.fill = GridBagConstraints.BOTH;
		gbc_genReport.gridwidth = 4;
		gbc_genReport.insets = new Insets(0, 0, 5, 5);
		gbc_genReport.gridx = 0;
		gbc_genReport.gridy = 14;
		contentPane.add(genReport, gbc_genReport);
		
		openTournyChart = new JButton("View Tournament Chart");
		openTournyChart.setToolTipText("Open a browser to view the tournament chart");
		openTournyChart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (Client.socket == null) {
					TextOutputUtils.appendText("[Error] Not connected to any tournament server!", true);
					return;
				}
				if (!Client.socket.isClosed()) {
					if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					    try {
							Desktop.getDesktop().browse(new URI("http://" + Client.host + ":8080"));
							return;
						} catch (IOException | URISyntaxException e1) {
							TextOutputUtils.appendText("Unable to open browser! You can go to http://<host>:8080 manually", true);
							return;
						}
					}
					TextOutputUtils.appendText("Unable to open browser! You can go to http://<host>:8080 manually", true);
					return;
				}
				TextOutputUtils.appendText("[Error] Not connected to any tournament server!", true);
			}
		});
		openTournyChart.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_openTournyChart = new GridBagConstraints();
		gbc_openTournyChart.gridwidth = 4;
		gbc_openTournyChart.fill = GridBagConstraints.BOTH;
		gbc_openTournyChart.insets = new Insets(0, 0, 5, 5);
		gbc_openTournyChart.gridx = 0;
		gbc_openTournyChart.gridy = 16;
		contentPane.add(openTournyChart, gbc_openTournyChart);
		commandInput.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_commandInput = new GridBagConstraints();
		gbc_commandInput.insets = new Insets(0, 0, 0, 5);
		gbc_commandInput.fill = GridBagConstraints.BOTH;
		gbc_commandInput.gridx = 5;
		gbc_commandInput.gridy = 19;
		contentPane.add(commandInput, gbc_commandInput);
		commandInput.setColumns(10);
		
		execCommand = new JButton("RUN");
		execCommand.setToolTipText("Execute a command");
		execCommand.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (commandInput.getText().equals("")) {
					return;
				}
				String cmd = commandInput.getText();
				Client.send(cmd);
				commandInput.setText("");
			}
		});
		execCommand.setFont(new Font("Tahoma", Font.PLAIN, 19));
		GridBagConstraints gbc_execCommand = new GridBagConstraints();
		gbc_execCommand.fill = GridBagConstraints.BOTH;
		gbc_execCommand.gridx = 6;
		gbc_execCommand.gridy = 19;
		contentPane.add(execCommand, gbc_execCommand);
	}

}
