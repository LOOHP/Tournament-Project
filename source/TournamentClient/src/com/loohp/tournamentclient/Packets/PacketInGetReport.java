package com.loohp.tournamentclient.Packets;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketInGetReport extends PacketIn {
	
	public final int packetId = 0x01;
	
	public int getPacketId() {
		return packetId;
	}
	
	public PacketInGetReport() {
		
	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		
		DataOutputStream output = new DataOutputStream(buffer);
		output.writeByte(packetId);
		
		return buffer.toByteArray();
	}

}
