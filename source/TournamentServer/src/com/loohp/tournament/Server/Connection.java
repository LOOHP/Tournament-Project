package com.loohp.tournament.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.loohp.tournament.Lang;
import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.ClientFunctions.FunctionGetReport;
import com.loohp.tournament.Utils.ArraysUtils;
import com.loohp.tournament.Utils.CustomHashMapUtils;
import com.loohp.tournament.Utils.IO;

public class Connection extends Thread {

    private Socket client_socket;

    public Connection(Socket client_socket) {
        this.client_socket = client_socket;
    }
    
    public Socket getSocket() {
    	return client_socket;
    }

    public void run() {

		Thread t = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	String clientLang = CustomHashMapUtils.serialize(Lang.clientLang);
		    	send("function:serverlang=" + clientLang);
		    	
		    	if (TournamentServer.activeCompetition.isPresent()) {
					send("function:getroundnum=" + TournamentServer.activeCompetition.get().getRounds().size());
				} else {
					send("function:getroundnum=-1");
				}
		    }
		});  
		t.start();
    	
    	try {
    		while (true) {
	    		client_socket.setKeepAlive(true);
	    		
	    		boolean done = false;
				String in = "";
				byte[] byteArray = new byte[0];
				while (!done) {
					DataInputStream dIn = new DataInputStream(client_socket.getInputStream());			
					byte[] each = new byte[dIn.readInt()];
					dIn.readFully(each);
					byteArray = ArraysUtils.joinArrayBytes(byteArray, each);
					in = new String(byteArray, StandardCharsets.UTF_8);
					if (in.startsWith("<Packet>") && in.endsWith("</Packet>")) {
						done = true;
					}
				}
				
				in = in.replaceAll("^<Packet>", "");
				in = in.replaceAll("<\\/Packet>$","");
	    		
	    		if (in.startsWith("function:")) {
	    			if (in.equals("function:getreport")) {
	    				FunctionGetReport.GenAndSendReport(this);
	    			} else if (in.startsWith("function:getroundnum")) {
	    				if (TournamentServer.activeCompetition.isPresent()) {
	    					send("function:getroundnum=" + TournamentServer.activeCompetition.get().getRounds().size());
	    				} else {
	    					send("function:getroundnum=-1");
	    				}
	    			}
	    		} else {
	    			String[] array = in.trim().replaceAll(" +", " ").split(" ");
	    			IO.input = Optional.of(array);
	    			IO.writeLn(in);
	    		}
    		}
    	} catch (IOException e) {
    		try {
				client_socket.close();
			} catch (IOException e1) {}
    		Server.clients.remove(this);
    		IO.writeLn("");
    		String str = client_socket.getInetAddress().getHostName() + ":" + client_socket.getPort();
			IO.writeLn(Lang.getLang("Server.Disconnect").replace("%s", str));
			IO.write("> ");
		}
    }
    
    public void send(String args) {
    	try {
    		client_socket.setKeepAlive(true);		
			
			args = "<Packet>" + args + "</Packet>";
			byte[] byteArray = args.getBytes(StandardCharsets.UTF_8);
			byte[][] splitArray = ArraysUtils.splitArrayBytes(byteArray, 32768);
			
			for (byte[] each : splitArray) {
				DataOutputStream dOut = new DataOutputStream(client_socket.getOutputStream());		
				dOut.writeInt(each.length);
				dOut.write(each);
				dOut.flush();
			}
	    } catch (IOException e) {
			try {
				client_socket.close();
			} catch (IOException e1) {}
			Server.clients.remove(this);
			IO.writeLn("");
			String str = client_socket.getInetAddress().getHostName() + ":" + client_socket.getPort();
			IO.writeLn(Lang.getLang("Server.Disconnect").replace("%s", str));
			IO.write("> ");
		}
    }
}