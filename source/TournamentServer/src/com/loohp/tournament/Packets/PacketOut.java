package com.loohp.tournament.Packets;

import java.io.IOException;

public abstract class PacketOut implements Packet {
	
	public abstract int getPacketId();
	
	public abstract byte[] getBytes() throws IOException;

}
