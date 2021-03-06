package net.herorat.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.herorat.Main;
import net.herorat.features.remote.Remote;
import net.herorat.features.screen.Screen;
import net.herorat.features.servers.Server;
import net.herorat.network.Network;


public class PanelScreen extends JPanel
{
	private static final long serialVersionUID = -4833291511811911810L;

	private JLabel label_select;
	public JButton button_start;
//	public String combo_selected_item = "";
	private boolean running = false;

	public JLabel label_screen;
	private JScrollPane scroll_screen;

    private Server server;

	private JLabel label_zoom;
	public JSpinner spinner_zoom;
	private JCheckBox box_remote;
	private JButton button_save;
	private FrameRemote frameRemote = null;
	
	public PanelScreen()
	{
		initComponents();
		display();
	}
	
	private void initComponents()
	{
		setLayout(new BorderLayout(5, 0));
		createSelect();
		createScreen();
		createZoom();
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
		

	}

    public Server getSelectedServer(){
        return server;
    }

	private void startService(){
        server = Main.mainWindow.ServerStatusTree.getSelectedServer();
        if(server !=null){
            running = true;
            button_start.setText("Stop service");
            label_select.setText("Talking to: "+server.getServerName()+"@"+server.getIp());
            if (frameRemote != null)
            {
                frameRemote.dispose();
                frameRemote = null;
            }

            if (server != null) Screen.send(server, Integer.valueOf(spinner_zoom.getValue().toString()));
        }else{
            stopService();
        }
    }

    private void stopService(){
        server = null;
        running = false;
        label_select.setText("Select a user from the tree list.");
        button_start.setText("Start service");
        label_screen.setIcon(null);
    }

	private void createScreen()
	{
		label_screen = new JLabel();
		label_screen.setDoubleBuffered(true);
		label_screen.setOpaque(true);
		label_screen.setBackground(new Color(66, 126, 199));
		scroll_screen = new JScrollPane();
		scroll_screen.setViewportView(label_screen);
		
		scroll_screen.addMouseWheelListener(new MouseWheelListener()
    	{
			public void mouseWheelMoved(MouseWheelEvent event)
			{
				int notches = event.getWheelRotation();
				if (notches < 0)
				{
					spinner_zoom.setValue(Integer.parseInt(spinner_zoom.getValue().toString()) + 1);
				}
				else
				{
					spinner_zoom.setValue(Integer.parseInt(spinner_zoom.getValue().toString()) - 1);
				}
			}
    	});
    	
		scroll_screen.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll_screen.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		
		HandScrollListener scrollListener = new HandScrollListener(label_screen);
		scroll_screen.getViewport().addMouseMotionListener(scrollListener);
		scroll_screen.getViewport().addMouseListener(scrollListener);
		
		add(scroll_screen, BorderLayout.CENTER);	
	}
	
	private void createZoom()
	{
		label_zoom = new JLabel("Zoom: ");
		spinner_zoom = new JSpinner();
		spinner_zoom.setModel(new SpinnerNumberModel(50, 1, 100, 1));
		box_remote = new JCheckBox("Remote Control");
		box_remote.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent evt)
			{
				if (box_remote.isSelected())
				{
					if (server != null)
					{
						frameRemote = new FrameRemote(server);
						
						scroll_screen.getViewport().addMouseMotionListener(null);
						scroll_screen.getViewport().addMouseListener(new MouseAdapter() {
							public void mousePressed(MouseEvent e)
							{
								Point point = e.getPoint();
								Remote.sendMouse(server, e.getButton(), point.x, point.y);
							}
	
							public void mouseReleased(MouseEvent e)
							{
							}
						});
					}
				}
				else if (frameRemote != null)
				{
					frameRemote.dispose();
					frameRemote = null;
					
					HandScrollListener scrollListener = new HandScrollListener(label_screen);
					scroll_screen.getViewport().addMouseMotionListener(scrollListener);
					scroll_screen.getViewport().addMouseListener(scrollListener);
				}
			}
		});
		
		button_save = new JButton("Save screen");
		button_save.setPreferredSize(new Dimension(100, 25));
		button_save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt)
			{
				ImageIcon img = (ImageIcon) Main.mainWindow.PanelScreen.label_screen.getIcon();
				if (img == null) return;
				
				Image image = img.getImage();
				RenderedImage rendered;
				if (image instanceof RenderedImage)
				{
					rendered = (RenderedImage)image;
				}
				else
				{
					BufferedImage buffered = new BufferedImage(img.getIconWidth(), img.getIconHeight(), BufferedImage.TYPE_INT_RGB);
					Graphics2D g = buffered.createGraphics();
					g.drawImage(image, 0, 0, null);
					g.dispose();
					rendered = buffered;
				}
				
				try
				{
					ImageIO.write(rendered, "JPEG", new File("screenshot_" + new Date().getTime() + ".jpg"));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
		
		JPanel panel_right = new JPanel();
		panel_right.setLayout(new BorderLayout(5, 0));
		panel_right.add(box_remote, BorderLayout.WEST);
		panel_right.add(button_save, BorderLayout.EAST);		
		
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new BorderLayout(5, 0));
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
		bottomPanel.add(label_zoom, BorderLayout.LINE_START);
		bottomPanel.add(spinner_zoom);
		bottomPanel.add(panel_right, BorderLayout.LINE_END);
		add(bottomPanel, BorderLayout.SOUTH);
	}
}