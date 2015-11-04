package net.herorat.utils;


import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
//	public static byte[] toByteArray(Object obj){
//		byte[] out;
//		try {
//			((DataInputStream) obj).readFully(out);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return out;
//	}
	
	public static byte[] toByteArray(InputStream obj){
				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[16384];

				try {
					while ((nRead = obj.read(data, 0, data.length)) != -1) {
					  buffer.write(data, 0, nRead);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				try {
					buffer.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				return buffer.toByteArray();
	}
}
