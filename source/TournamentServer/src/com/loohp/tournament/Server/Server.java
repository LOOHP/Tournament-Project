package com.loohp.tournament.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.GUI.ClientTextOutput;
import com.loohp.tournament.Utils.IO;

public class Server extends Thread {

	private List<Connection> clients = new CopyOnWriteArrayList<Connection>();
	private ServerSocket serverSocket;
	private int serverPort;
	
	public Server(int port) {
		this.serverPort = port;
		start();
	}
	
	public void run() {
		Thread t1 = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	while (true) {
			    	String str = "";
					for (Connection client : clients) {
						str = str + client.getSocket().getInetAddress().getHostName() + "\n";
					}
					ClientTextOutput.setText(str);
					try {TimeUnit.MILLISECONDS.sleep(1000);} catch (InterruptedException e) {}
		    	}
		    }
		});  
		t1.start();
		
		try {
			serverSocket = new ServerSocket(serverPort);
			IO.writeLn("Tournament System Server listening on [" + serverSocket.getInetAddress().getHostName() + ":" + serverSocket.getLocalPort() + "]");
	        while (true) {
	            Socket connection = serverSocket.accept();
	            IO.writeLn("");
	            String str = connection.getInetAddress().getHostName() + ":" + connection.getPort();
				IO.writeLn(TournamentServer.getInstance().getLang().get("Server.Connect").replace("%s", str));
				IO.write("> ");
	            Connection sc = new Connection(connection);
	            clients.add(sc);
	            sc.start();
	        }
	    } catch(IOException e) {
	        e.printStackTrace();
	    }
	}
	
	public List<Connection> getClients() {
		return clients;
	}

	public ServerSocket getServerSocket() {
		return serverSocket;
	}

}
