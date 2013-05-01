import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JSplitPane;
import java.awt.Component;
import javax.swing.Box;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.JProgressBar;
import javax.swing.border.MatteBorder;


public class ClientMain extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField username;
	private JPasswordField password;
	private JTextField newUsername;
	private JPasswordField newPassword;
	private JPasswordField newPasswordConf;
	private JTextField email;
	private JLabel lblUsername;
	private JLabel lblPassword;
	private JLabel lblUsername_1;
	private JLabel lblPassword_1;
	private JLabel lblConfirmPassword;
	private JLabel lblEmailAddress;
	private JLabel lblNewLabel;
	private JLabel lblCreateNewAccount;
	private JLabel loginError;
	private JLabel signupError;
	private JButton btnLogin;
	private JButton btnSignUp;
	private JProgressBar loginProg;
	private JProgressBar signupProg;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientMain frame = new ClientMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		
		if (action.equals("Login")) {
			getLoginError().setText("");
			String uname = username.getText();
			String pass = password.getText();
			
			if (uname.equals("") && pass.equals("")) {
				System.out.println("HERE");
				getLoginError().setText("Username and password field empty");
				return;
			}
			else if (uname.equals("")) {
				getLoginError().setText("Username field empty");
				return;
			}
			else if (pass.equals("")) {
				getLoginError().setText("Password field empty");
				return;
			}
			
			enableComponents(false);
			getBtnLogin().setVisible(false);
			getLoginProg().setVisible(true);
			
			Thread cl = new Thread(new ClientLogin(uname, pass, this));
			cl.start();
		}
		
		else if (action.equals("Sign Up")) {
			String newUname = newUsername.getText();
			String newPass = newPassword.getText();
			String newPassConf = newPasswordConf.getText();
			String newEmail = email.getText();
		}	
	}
	
	public void enableComponents(boolean enable) {
		
		username.setEditable(enable);
		password.setEditable(enable);;
		newUsername.setEditable(enable);;
		newPassword.setEditable(enable);;
		newPasswordConf.setEditable(enable);
		email.setEditable(enable);
		getBtnLogin().setEnabled(enable);
		btnSignUp.setEnabled(enable);
		
	}
	
	public ClientMain() {
		super("Let Me In Login");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 594, 292);
		contentPane = new JPanel();
		contentPane.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		username = new JTextField();
		username.setBounds(96, 91, 132, 20);
		contentPane.add(username);
		username.setColumns(10);
		
		password = new JPasswordField();
		password.setBounds(96, 122, 132, 20);
		contentPane.add(password);
		
		newUsername = new JTextField();
		newUsername.setBounds(415, 62, 126, 20);
		contentPane.add(newUsername);
		newUsername.setColumns(10);
		
		newPassword = new JPasswordField();
		newPassword.setBounds(415, 93, 126, 20);
		contentPane.add(newPassword);
		
		newPasswordConf = new JPasswordField();
		newPasswordConf.setBounds(415, 124, 126, 20);
		contentPane.add(newPasswordConf);
		
		email = new JTextField();
		email.setBounds(415, 155, 126, 20);
		contentPane.add(email);
		email.setColumns(10);
		
		setBtnLogin(new JButton("Login"));
		getBtnLogin().addActionListener(this);
		getBtnLogin().setFont(new Font("Calibri", Font.PLAIN, 13));
		getBtnLogin().setBounds(81, 154, 89, 23);
		contentPane.add(getBtnLogin());
		
		btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(this);
		btnSignUp.setFont(new Font("Calibri", Font.PLAIN, 13));
		btnSignUp.setBounds(378, 184, 89, 23);
		contentPane.add(btnSignUp);
		
		lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblUsername.setBounds(28, 94, 67, 14);
		contentPane.add(lblUsername);
		
		lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblPassword.setBounds(28, 125, 67, 14);
		contentPane.add(lblPassword);
		
		lblUsername_1 = new JLabel("Desired username:");
		lblUsername_1.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblUsername_1.setBounds(306, 62, 106, 14);
		contentPane.add(lblUsername_1);
		
		lblPassword_1 = new JLabel("Password:");
		lblPassword_1.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblPassword_1.setBounds(306, 96, 89, 14);
		contentPane.add(lblPassword_1);
		
		lblConfirmPassword = new JLabel("Confirm password:");
		lblConfirmPassword.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblConfirmPassword.setBounds(306, 124, 107, 14);
		contentPane.add(lblConfirmPassword);
		
		lblEmailAddress = new JLabel("Email address:");
		lblEmailAddress.setFont(new Font("Calibri", Font.PLAIN, 12));
		lblEmailAddress.setBounds(306, 155, 89, 14);
		contentPane.add(lblEmailAddress);
		
		lblNewLabel = new JLabel("Login");
		lblNewLabel.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblNewLabel.setBounds(98, 65, 130, 14);
		contentPane.add(lblNewLabel);
		
		lblCreateNewAccount = new JLabel("Create New Account");
		lblCreateNewAccount.setFont(new Font("Calibri", Font.PLAIN, 15));
		lblCreateNewAccount.setBounds(360, 34, 156, 14);
		contentPane.add(lblCreateNewAccount);
		
		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setBackground(Color.GRAY);
		separator.setForeground(Color.GRAY);
		separator.setBounds(271, 11, 1, 242);
		contentPane.add(separator);
		
		setLoginError(new JLabel(""));
		getLoginError().setHorizontalAlignment(SwingConstants.CENTER);
		getLoginError().setFont(new Font("Calibri", Font.PLAIN, 13));
		getLoginError().setForeground(Color.RED);
		getLoginError().setBounds(17, 188, 211, 14);
		contentPane.add(getLoginError());
		
		signupError = new JLabel("");
		signupError.setHorizontalAlignment(SwingConstants.CENTER);
		signupError.setFont(new Font("Calibri", Font.PLAIN, 13));
		signupError.setForeground(Color.RED);
		signupError.setBounds(306, 218, 238, 14);
		contentPane.add(signupError);
		
		setLoginProg(new JProgressBar());
		getLoginProg().setVisible(false);
		getLoginProg().setIndeterminate(true);
		getLoginProg().setBounds(81, 161, 89, 14);
		contentPane.add(getLoginProg());
		
		signupProg = new JProgressBar();
		signupProg.setVisible(false);
		signupProg.setIndeterminate(true);
		signupProg.setBounds(378, 193, 89, 14);
		contentPane.add(signupProg);
	}

	public JButton getBtnLogin() {
		return btnLogin;
	}

	public void setBtnLogin(JButton btnLogin) {
		this.btnLogin = btnLogin;
	}

	public JProgressBar getLoginProg() {
		return loginProg;
	}

	public void setLoginProg(JProgressBar loginProg) {
		this.loginProg = loginProg;
	}

	public JLabel getLoginError() {
		return loginError;
	}

	public void setLoginError(JLabel loginError) {
		this.loginError = loginError;
	}
}
