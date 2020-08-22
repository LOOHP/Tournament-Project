package com.loohp.tournamentclient.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketInGetRoundNumber extends PacketIn {
	
	public final int packetId = 0x02;
	
	public int getPacketId() {
		return packetId;
	}
	
	public PacketInGetRoundNumber() {
		
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		DataOutputStream output = new DataOutputStream(buffer);
		output.writeByte(packetId);
		
		return buffer.toByteArray();
	}

}
