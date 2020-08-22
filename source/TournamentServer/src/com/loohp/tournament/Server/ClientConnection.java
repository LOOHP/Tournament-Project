package com.loohp.tournament.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

import com.loohp.tournament.TournamentServer;
import com.loohp.tournament.ClientFunctions.FunctionGetReport;
import com.loohp.tournament.Packets.PacketInCommand;
import com.loohp.tournament.Packets.PacketInGetReport;
import com.loohp.tournament.Packets.PacketInGetRoundNumber;
import com.loohp.tournament.Packets.PacketOut;
import com.loohp.tournament.Packets.PacketOutLanguage;
import com.loohp.tournament.Packets.PacketOutRoundNumber;
import com.loohp.tournament.Utils.IO;

public class ClientConnection extends Thread {

    private Socket client_socket;
    private DataOutputStream out;
    private DataInputStream in;

    public ClientConnection(Socket client_socket) {
        this.client_socket = client_socket;
        try {
			this.out = new DataOutputStream(client_socket.getOutputStream());
			this.in = new DataInputStream(client_socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}	
    }
    
    public Socket getSocket() {
    	return client_socket;
    }

    public void run() {

		Thread t = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	send(new PacketOutLanguage(TournamentServer.getInstance().getLang().getClientLang()));
		    	
		    	if (TournamentServer.getInstance().hasActiveCompetition()) {
					send(new PacketOutRoundNumber(TournamentServer.getInstance().getActiveCompetition().getRounds().size()));
				} else {
					send(new PacketOutRoundNumber(-1));
				}
		    }
		});  
		t.start();
    	
    	try {
    		while (true) {
	    		client_socket.setKeepAlive(true);
	    		
	    		int size = in.readInt();
	    		int packetId = in.readByte();
	    		
	    		if (PacketInCommand.getPacketId() == packetId) {
	    			PacketInCommand packet = new PacketInCommand(in);
	    			String[] array = packet.getText().trim().replaceAll(" +", " ").split(" ");
	    			IO.input = Optional.of(array);
	    			IO.writeLn(packet.getText());
	    		} else if (PacketInGetReport.getPacketId() == packetId) {
	    			//PacketInGetReport packet = new PacketInGetReport(in);
	    			FunctionGetReport.GenAndSendReport(this);
	    		} else if (PacketInGetRoundNumber.getPacketId() == packetId) {
	    			//PacketInGetRoundNumber packet = new PacketInGetRoundNumber(in);
	    			if (TournamentServer.getInstance().hasActiveCompetition()) {
    					send(new PacketOutRoundNumber(TournamentServer.getInstance().getActiveCompetition().getRounds().size()));
    				} else {
    					send(new PacketOutRoundNumber(-1));
    				}
	    		} else {
	    			in.skipBytes(size - 1);
	    		}
    		}
    	} catch (IOException e) {
    		try {
				client_socket.close();
			} catch (IOException e1) {}
    		TournamentServer.getInstance().getServer().getClients().remove(this);
    		IO.writeLn("");
    		String str = client_socket.getInetAddress().getHostName() + ":" + client_socket.getPort();
			IO.writeLn(TournamentServer.getInstance().getLang().get("Server.Disconnect").replace("%s", str));
			IO.write("> ");
		}
    }
    
    public void send(PacketOut packet) {
    	try {
    		client_socket.setKeepAlive(true);		
			
    		byte[] bytes = packet.getBytes();
	
			out.writeInt(bytes.length);
			out.write(bytes);
			out.flush();
	    } catch (IOException e) {
			try {
				client_socket.close();
			} catch (IOException e1) {}
			TournamentServer.getInstance().getServer().getClients().remove(this);
			IO.writeLn("");
			String str = client_socket.getInetAddress().getHostName() + ":" + client_socket.getPort();
			IO.writeLn(TournamentServer.getInstance().getLang().get("Server.Disconnect").replace("%s", str));
			IO.write("> ");
		}
    }
}