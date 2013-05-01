import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CheckboxMenuItem;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Canvas;
import javax.swing.JScrollPane;
import java.awt.Panel;
import java.awt.GridLayout;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.TitledBorder;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.TextArea;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.CardLayout;


public class ClientHome extends JFrame implements ActionListener, WindowListener, MouseListener {

	private JPanel contentPane;
	JPanel panel;
	TrayIcon trayIcon = null;
	BufferedImage clientLive;
	boolean notified = false;
	String uname;
	
	public ClientHome(String uname) {
		super("Let Me In Control");
		this.uname = uname;
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 778, 478);
		setMinimumSize(getSize());
		contentPane = new JPanel();
		contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{379, 379, 0};
		gbl_contentPane.rowHeights = new int[]{262, 176, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Account Info", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.BOTH;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 0;
		gbc_panel_1.gridy = 0;
		contentPane.add(panel_1, gbc_panel_1);
		
		panel = new panel(this);
		panel.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Live Image", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.insets = new Insets(0, 0, 5, 0);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 0;
		contentPane.add(panel, gbc_panel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Console", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.fill = GridBagConstraints.BOTH;
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 1;
		contentPane.add(panel_2, gbc_panel_2);
		panel_2.setLayout(new CardLayout(0, 0));
		
		String user = System.getProperty("user.name");
	
		TextArea textArea = new TextArea(console(), 0, 0, 3);
		textArea.setEditable(false);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.GREEN);
		textArea.setFont(new Font("Calibri", Font.PLAIN, 12));
		panel_2.add(textArea, "name_17937718646748");
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Faces", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 1;
		gbc_panel_3.gridy = 1;
		contentPane.add(panel_3, gbc_panel_3);
		panel_3.setLayout(new GridLayout(1, 0, 0, 0));
		addWindowListener(this);
		setIconImage(Toolkit.getDefaultToolkit().getImage("res/img/door-icon.png"));
		
        if (SystemTray.isSupported()) {
            sysTray();
        }
        
        Thread fr = new Thread(new FacialRecog(this));
        fr.start();
	}
	
	private String console() {
		String prefix = "[" + uname + "@lmi ~]$";
		return prefix;
	}


	private void sysTray() {

		final PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("res/img/door-tray.png"));
        final SystemTray tray = SystemTray.getSystemTray();
        
        MenuItem openItem = new MenuItem("Open");
        openItem.addActionListener(this);
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(this);
       
        popup.add(openItem);
        popup.addSeparator();
        popup.add(exitItem);
       
        trayIcon.setPopupMenu(popup);
        
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
        
        trayIcon.addMouseListener(this);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		if (action.equals("Exit")){
			System.exit(0);
		}
		if (action.equals("Open")) {
			setVisible(true);
		}
	
	}

	public JPanel getpanel() {
		return panel;
	}
	
	public BufferedImage getImage() {
		return clientLive;
	}
	
	public void setImage(BufferedImage clientLive) {
		this.clientLive = clientLive;
		panel.repaint();
	}
	
	
	@Override
	public void windowClosing(WindowEvent e) {
		if (!notified) {
			trayIcon.displayMessage("Let Me In", "Let Me In will continue to run in tray", TrayIcon.MessageType.INFO);
			notified = true;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (!isVisible())
			setVisible(true);
		else
			setVisible(false);
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}

class panel extends JPanel {
	
	ClientHome clientHome;
	BufferedImage clientLive;
	panel(ClientHome clientHome) {
		//setPreferredSize(new Dimension(700, 400))
		this.clientHome = clientHome;
		setVisible(true);
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		clientLive = clientHome.getImage();
		if (clientLive != null) {
			Image clientLiveScaled = clientLive.getScaledInstance(this.getWidth() - 10, this.getHeight() - 20, Image.SCALE_SMOOTH);
			g2d.drawImage(clientLiveScaled, 5, 15, null);
		}
	}
}
	
