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
	
	String VERSIONCODE = "1.0";
	private JPanel contentPane;
	JPanel panel, panel_4, panel_5, panel_6, panel_7, panel_8, panel_9;
	TrayIcon trayIcon = null;
	BufferedImage clientLive;
	boolean notified = false;
	String uname;
	private BufferedImage faceImage1;
	private BufferedImage faceImage2;
	private BufferedImage faceImage3;
	private BufferedImage faceImage4;
	private BufferedImage faceImage5;
	private BufferedImage faceImage6;
	
	public ClientHome(String uname) {
		super("Let Me In Client Control");
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
	
		TextArea textArea = new TextArea(console() + "Welcome to Let Me In Client v" + VERSIONCODE + "\n", 0, 0, 3);
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
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{1.0, 1.0, 1.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		panel_4 = new facePanel(this, 1);
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.insets = new Insets(0, 0, 5, 5);
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.gridx = 0;
		gbc_panel_4.gridy = 0;
		panel_3.add(panel_4, gbc_panel_4);
		
		panel_5 = new facePanel(this, 2);
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.insets = new Insets(0, 0, 5, 5);
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 0;
		panel_3.add(panel_5, gbc_panel_5);
		
		panel_6 = new facePanel(this, 3);
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.insets = new Insets(0, 0, 5, 0);
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.gridx = 2;
		gbc_panel_6.gridy = 0;
		panel_3.add(panel_6, gbc_panel_6);
		
		panel_7 = new facePanel(this, 4);
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.insets = new Insets(0, 0, 0, 5);
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 1;
		panel_3.add(panel_7, gbc_panel_7);
		
		panel_8 = new facePanel(this, 5);
		GridBagConstraints gbc_panel_8 = new GridBagConstraints();
		gbc_panel_8.insets = new Insets(0, 0, 0, 5);
		gbc_panel_8.fill = GridBagConstraints.BOTH;
		gbc_panel_8.gridx = 1;
		gbc_panel_8.gridy = 1;
		panel_3.add(panel_8, gbc_panel_8);
		
		panel_9 = new facePanel(this, 6);
		GridBagConstraints gbc_panel_9 = new GridBagConstraints();
		gbc_panel_9.fill = GridBagConstraints.BOTH;
		gbc_panel_9.gridx = 2;
		gbc_panel_9.gridy = 1;
		panel_3.add(panel_9, gbc_panel_9);
		addWindowListener(this);
		setIconImage(Toolkit.getDefaultToolkit().getImage("res/img/door-icon.png"));
		
        if (SystemTray.isSupported()) {
            sysTray();
        }
        
        Thread fr = new Thread(new FacialRecog(this));
        fr.start();
	}
	
	private String console() {
		String prefix = "[" + uname + "@lmi ~]$ ";
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
	
	
	
	public BufferedImage getFace1() {
		return faceImage1;
	}
	
	public void setFace1(BufferedImage faceImage1) {
		this.faceImage1 = faceImage1;
		panel_4.repaint();
	}
	
	public BufferedImage getFace2() {
		return faceImage2;
	}
	
	public void setFace2(BufferedImage faceImage2) {
		this.faceImage2 = faceImage2;
		panel_5.repaint();
	}
	
	public BufferedImage getFace3() {
		return faceImage3;
	}
	
	public void setFace3(BufferedImage faceImage3) {
		this.faceImage3 = faceImage3;
		panel_6.repaint();
	}
	
	public BufferedImage getFace4() {
		return faceImage4;
	}
	
	public void setFace4(BufferedImage faceImage4) {
		this.faceImage4 = faceImage4;
		panel_7.repaint();
	}
	
	public BufferedImage getFace5() {
		return faceImage5;
	}
	
	public void setFace5(BufferedImage faceImage5) {
		this.faceImage5 = faceImage5;
		panel_8.repaint();
	}
	
	public BufferedImage getFace6() {
		return faceImage6;
	}
	
	public void setFace6(BufferedImage faceImage6) {
		this.faceImage6 = faceImage6;
		panel_9.repaint();
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

	public void resetFaces() {
			setFace1(null);
			setFace2(null);
			setFace3(null);
			setFace4(null);
			setFace5(null);
			setFace6(null);
			
			panel_4.repaint();
			panel_5.repaint();
			panel_6.repaint();
			panel_7.repaint();
			panel_8.repaint();
			panel_9.repaint();
	
	}
}

//FIX FOLLOWING HACK TO IMPLEMET INHERITANCE


class facePanel extends JPanel {
	ClientHome clientHome;
	BufferedImage faceImage;
	int getfaceno;
	
	facePanel(ClientHome clientHome, int getfaceno) {
		this.clientHome = clientHome;
		setVisible(true);
		this.getfaceno = getfaceno;
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if (getfaceno == 1)
			faceImage = clientHome.getFace1();
		else if (getfaceno == 2)
			faceImage = clientHome.getFace2();
		else if (getfaceno == 3)
			faceImage = clientHome.getFace3();
		else if (getfaceno == 4)
			faceImage = clientHome.getFace4();
		else if (getfaceno == 5)
			faceImage = clientHome.getFace5();
		else if (getfaceno == 6)
			faceImage = clientHome.getFace6();
		
		if (faceImage != null) {
			Image faceScaled = faceImage.getScaledInstance(this.getWidth() - 6, this.getHeight() - 6, Image.SCALE_SMOOTH);
			g2d.drawImage(faceScaled, 3, 3, null);
		}
		else
			g2d.dispose();
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
	
