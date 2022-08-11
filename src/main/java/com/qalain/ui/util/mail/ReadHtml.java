package com.qalain.ui.util.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReadHtml {

	public static String readString(String fileIn) {
	    String str = "";
	    File file = new File(fileIn);
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
	        int size = in.available();
	        byte[] buffer = new byte[size];
	        in.read(buffer);
	        str = new String(buffer, StandardCharsets.UTF_8);
	    } catch (IOException e) {
	        return null;
	    } finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	    return str;
	}
}
