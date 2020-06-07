package com.loohp.tournamentclient.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Base64;

public class CustomHashMapUtils {
	
	public static String serialize(Serializable o) {
		try {
		    ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    ObjectOutputStream oos = new ObjectOutputStream(baos);
		    oos.writeObject(o);
		    oos.close();
		    return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (Exception e) {
			TextOutputUtils.appendText("Unable to decode lang pack", true);
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			TextOutputUtils.appendText(errors.toString(), true);
		}
		return null;
	}
	
	public static Object deserialize(String s) {
		try {
			byte[] data = Base64.getDecoder().decode(s);
			ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
			Object o = ois.readObject();
			ois.close();
			return o;
		} catch (Exception e) {
			TextOutputUtils.appendText("Unable to decode lang pack", true);
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			TextOutputUtils.appendText(errors.toString(), true);
		}
		return null;
	}

}
