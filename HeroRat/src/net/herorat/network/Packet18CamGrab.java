package net.herorat.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public class Packet18CamGrab extends Packet {

	public Packet18CamGrab(){
		super(18);
	}
	
	public Packet18CamGrab(DataInputStream inputstream) {
		super(inputstream, 18);
		// TODO Auto-generated constructor stub
	}
	
	public Packet18CamGrab(DataOutputStream outputstream, String[] objects)	{
		super(outputstream, 18, objects);
	}
}
