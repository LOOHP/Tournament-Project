package com.loohp.tournament.Packets;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketInGetRoundNumber extends PacketIn {
	
	public final int packetId = 0x02;
	
	public static int getPacketId() {
		return new PacketInGetRoundNumber().packetId;
	}
	
	private PacketInGetRoundNumber() {}
	
	public PacketInGetRoundNumber(DataInputStream in) throws IOException {
		
	}

}
