package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import net.herorat.Main;
import net.herorat.features.servers.Server;
import net.herorat.features.system.System;
import net.herorat.network.Network;


public class PanelSystem extends JPanel
{
	private static final long serialVersionUID = 5391500086140935512L;

	private JLabel label_select;
	private JButton button_start;
	private boolean running;
	private Server server;

	private JScrollPane scroll_output;
	public JTextArea area_output;

	public PanelSystem()
	{
		initComponents();
		display();
	}
	
	private void initComponents()
	{
		setLayout(new BorderLayout(5, 0));
		createSelect();
		createOutput();
	}
	
	private void display()
	{
		setVisible(true);
	}
	
	private void createSelect()
	{
		label_select = new JLabel("Select a user from the tree list.");
		button_start = new JButton("Start service");
		button_start.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(running){
					stopService();
				}else{
					startService();
				}
			}
		});

		JPanel top_panel = new JPanel();
		top_panel.setLayout(new BorderLayout(5, 0));
		top_panel.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 2));
		top_panel.add(label_select, BorderLayout.LINE_START);
		top_panel.add(button_start, BorderLayout.LINE_END);
		add(top_panel, BorderLayout.NORTH);

	}

    private void startService(){
        server = Main.mainWindow.ServerStatusTree.getSelectedServer();
        if (server != null) {
            running = true;
            button_start.setText("Stop service");
            label_select.setText("Talking to: "+server.getServerName()+"@"+server.getIp());
            area_output.setText("Loading ...");
            System.send(server);
        }else{
            stopService();
        }
    }

    private void stopService(){
        server = null;
        running = false;
        button_start.setText("Start service");
        label_select.setText("Select a user from the tree list.");
        area_output.setText("");
    }


    public Server getCurrentServer(){
        return server;
    }

	private void createOutput()
	{
		scroll_output = new JScrollPane();
		area_output = new JTextArea();
		area_output.setColumns(55);
		area_output.setRows(22);
		area_output.setEditable(false);
		scroll_output.setViewportView(area_output);
		add(scroll_output, BorderLayout.CENTER);
	}
}