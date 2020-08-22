package com.loohp.tournament.Packets;

import java.io.DataInputStream;
import java.io.IOException;

public class PacketInGetReport extends PacketIn {
	
	public final int packetId = 0x01;
	
	public static int getPacketId() {
		return new PacketInGetReport().packetId;
	}
	
	private PacketInGetReport() {}
	
	public PacketInGetReport(DataInputStream in) throws IOException {
		
	}

}
