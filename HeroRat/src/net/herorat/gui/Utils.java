package net.herorat.gui;


import java.io.ByteArrayOutputStream;

public class Utils {
	public static byte[] toByteArray(Object obj){
		return ((ByteArrayOutputStream) obj).toByteArray();
	}
}
