package com.loohp.tournamentclient.Packets;

import java.io.IOException;

public abstract class PacketIn implements Packet {
	
	public abstract int getPacketId();
	
	public abstract byte[] getBytes() throws IOException;

}
