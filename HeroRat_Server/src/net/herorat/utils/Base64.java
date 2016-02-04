package net.herorat.utils;

import javax.xml.bind.DatatypeConverter;

public class Base64 {
	public static String encode(byte[] str){
		return DatatypeConverter.printBase64Binary(str);
	}
	
	public static byte[] decode(String buff){
		return DatatypeConverter.parseBase64Binary(buff);
	}
}
