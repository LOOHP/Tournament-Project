package com.loohp.tournamentclient.Utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class DataTypeIO {
	
	public static void writeStringMapping(DataOutputStream out, Map<String, String> mapping, Charset charset) throws IOException {
		out.writeInt(mapping.size());
		for (Entry<String, String> entry : mapping.entrySet()) {
			writeString(out, entry.getKey(), charset);
			writeString(out, entry.getValue(), charset);
		}
	}
	
	public static Map<String, String> readStringMapping(DataInputStream in) throws IOException {
		int size = in.readInt();
		Map<String, String> mapping = new LinkedHashMap<>();
		for (int i = 0; i < size; i++) {
			String key = readString(in);
			String value = readString(in);
			mapping.put(key, value);
		}
		return mapping;
	}
	
	public static String readString(DataInputStream in) throws IOException {
		int length = in.readInt();

	    if (length == -1) {
	        throw new IOException("Premature end of stream.");
	    }

	    byte[] b = new byte[length];
	    in.readFully(b);
	    return new String(b);
	}
	
	public static int getStringLength(String string, Charset charset) throws IOException {
	    byte[] bytes = string.getBytes(charset);
	    return bytes.length;
	}
	
	public static void writeString(DataOutputStream out, String string, Charset charset) throws IOException {
	    byte[] bytes = string.getBytes(charset);
	    out.writeInt(bytes.length);
	    out.write(bytes);
	}

}
