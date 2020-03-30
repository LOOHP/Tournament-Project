package com.loohp.tournament.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.loohp.tournament.Lang;
import com.loohp.tournament.Tournament;
import com.loohp.tournament.GUI.ClientTextOutput;
import com.loohp.tournament.Utils.IO;

public class Server {
	
	public static List<Connection> clients = new CopyOnWriteArrayList<Connection>();
	public static ServerSocket serverSocket;
	
	public static void start() {
		
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
			serverSocket = new ServerSocket(Tournament.serverPort);
			IO.writeLn("Tournament System Server listening on [" + serverSocket.getInetAddress().getHostName() + ":" + serverSocket.getLocalPort() + "]");
	        while (true) {
	            Socket connection = serverSocket.accept();
	            IO.writeLn("");
	            String str = connection.getInetAddress().getHostName() + ":" + connection.getPort();
				IO.writeLn(Lang.getLang("Server.Connect").replace("%s", str));
				IO.write(">");
	            Connection sc = new Connection(connection);
	            clients.add(sc);
	            sc.start();
	        }
	    } catch(IOException e) {
	        e.printStackTrace();
	    }
	}

}
