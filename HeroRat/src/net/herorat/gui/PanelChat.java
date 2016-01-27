package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;

import net.herorat.Main;
import net.herorat.features.chat.Chat;
import net.herorat.features.servers.Server;


public class PanelChat extends JPanel
{
	private static final long serialVersionUID = 5391500086140935512L;

    // false == offline
    private boolean running = false;

	private JLabel label_select;
	private JButton button_start;
	
	private JScrollPane scroll_chat;
	public JTextArea area_chat;
	
	private JScrollPane scroll_input;
	private JTextArea area_input;
	private JButton button_input;
	
	private Server server = null;
	private String username = "Admin";

	public PanelChat()
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
//		combo_select = new JComboBox<String>( Network.getServerList(false) );
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
        top_panel.add(button_start,BorderLayout.LINE_END);
//		top_panel.add(combo_select);
		add(top_panel, BorderLayout.NORTH);
//		combo_select.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0)
//			{
//				String selection = String.valueOf(combo_select.getSelectedItem());
//				if (combo_select.getSelectedIndex() != 0 && !selection.equals(combo_selected_item))
//				{
//					combo_selected_item = selection;
//
//					server = Network.findWithCombo(combo_selected_item);
//					if (server != null) area_chat.setText(server.buffer_chat.toString());
//				}
//				else if (combo_select.getSelectedIndex() == 0)
//				{
//					combo_selected_item = "";
//					area_chat.setText("");
//				}
//			}
//		});
	}

    private void stopService(){
        server = null;
        label_select.setText("Select a user from the tree list.");
        button_start.setText("Start service");
        running = false;
    }

    private void startService(){
        server = Main.mainWindow.ServerStatusTree.getSelectedServer();
        if (server != null) {
            button_start.setText("Stop service");
            label_select.setText("Talking to: "+server.getServerName()+"@"+server.getIp());
            area_chat.setText(server.buffer_chat.toString());
            running = true;
        }else{
            area_chat.setText("Select an active server");
        }
    }

	private void createOutput()
	{
		scroll_chat = new JScrollPane();
		area_chat = new JTextArea();
		area_chat.setColumns(55);
		area_chat.setRows(19);
		area_chat.setEditable(false);
		scroll_chat.setViewportView(area_chat);
		add(scroll_chat, BorderLayout.CENTER);
	}
	
	private void createInput()
	{	
		scroll_input = new JScrollPane();
		area_input = new JTextArea();
		area_input.setRows(4);
		area_input.setLineWrap(true);
		scroll_input.setViewportView(area_input);

		InputMap input = area_input.getInputMap();
		KeyStroke enter = KeyStroke.getKeyStroke("ENTER");
		KeyStroke shiftEnter = KeyStroke.getKeyStroke("shift ENTER");
		input.put(shiftEnter, "insert-break");
		input.put(enter, "text-submit");
		
		ActionMap actions = area_input.getActionMap();
		actions.put("text-submit", new AbstractAction() {
			private static final long serialVersionUID = -6749249301370344488L;

			public void actionPerformed(ActionEvent e)
			{
				sendChatMsg();
			}
		});

		area_input.requestFocusInWindow();
		
		button_input = new JButton("Send");
		button_input.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				sendChatMsg();
			}
		});
		
		JPanel bottom_panel = new JPanel();
		TitledBorder titled = new TitledBorder("Message");
		bottom_panel.setBorder(titled);
		bottom_panel.setLayout(new BorderLayout(5, 0));
		bottom_panel.setBorder(BorderFactory.createEmptyBorder(10, 2, 5, 2));
		bottom_panel.add(scroll_input);
		bottom_panel.add(button_input, BorderLayout.LINE_END);
		add(bottom_panel, BorderLayout.SOUTH);
	}

    public void flushChat(Server sourceServer,String chat){
        if(sourceServer.equals(server)) area_chat.setText(chat);
    }
	
	private void sendChatMsg()
	{
		if (server != null)
		{
			if (area_input.getText().contains("/nick"))
			{
				username = area_input.getText().substring(6).equals("") ? "Admin" : area_input.getText().substring(6);
				server.buffer_chat.append("Your nickname has changed to " + username + "\n");
				area_chat.setText(server.buffer_chat.toString());
			}
			else if (!area_input.getText().equals(""))
			{
				server.buffer_chat.append("Me: " + area_input.getText() + "\n");
				Chat.send(server, username, area_input.getText());
				area_chat.setText(server.buffer_chat.toString());
			}
		}
		else
		{
			area_chat.setText(area_chat.getText() + "> No connection found!\n");
		}
		area_input.setText("");
	}

}