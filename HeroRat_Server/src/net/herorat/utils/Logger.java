package net.herorat.utils;

public class Logger {
	public static boolean on = false;
	
	public static void log(String msg,Object[] args){
		if(on) System.out.printf(msg,(Object[])args);
	}

	public static void log(String msg, Object args) {
		if (on) System.out.printf(msg,args);
	}
	
	public static void log(String msg){
		if(on) System.out.printf(msg);
	}
	
	public static void log(String msg,Exception e){
		if(on){
			System.out.printf(msg);
			e.printStackTrace();
		}
	}
}
