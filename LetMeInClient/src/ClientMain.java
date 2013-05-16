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
/**
 * @author Philip Leonard
 */

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
			getSignupError().setForeground(Color.RED);
			getSignupError().setText("");
			String newUnameText = getNewUsername().getText();
			String newPassText = getNewPassword().getText();
			String newPassConfText = getNewPasswordConf().getText();
			String newEmailText = getEmail().getText();
			
			if(verify(newUnameText, newPassText, newPassConfText, newEmailText)) {
				enableComponents(false);
				getBtnSignUp().setVisible(false);
				getSignupProg().setVisible(true);
				
				Thread cs = new Thread(new ClientSignup(newUnameText, newPassText, newEmailText, this));
				cs.start();
			}
			
		}	
	}
	
	private boolean verify(String newUnameText, String newPassText, String newPassConfText, String newEmailText) {
		int emptyCount = 0;
		boolean outcome = true;
		if (!newPassText.equals(newPassConfText)) {
			getSignupError().setText("Passwords don't match");
			outcome = false;
		}
		
		if (newUnameText.equals("")) {
			getSignupError().setText("Username field empty");
			emptyCount++;
			System.out.println("HERE");
			outcome = false;
		}
		if (newPassText.equals("")) {
			getSignupError().setText("Password field empty");
			emptyCount++;
			outcome = false;
		}
			
		if (newPassConfText.equals("")) {
			getSignupError().setText("Password confirm field empty");
			emptyCount++;
			outcome = false;
		}
			
		if (newEmailText.equals("")) {
			getSignupError().setText("Email field empty");
			emptyCount++;
			outcome = false;
		}
			
		if (emptyCount > 1) {
			getSignupError().setText("Multiple fields empty");
			outcome = false;
		}
		return outcome;
	}

	public void enableComponents(boolean enable) {
		
		username.setEditable(enable);
		password.setEditable(enable);;
		getNewUsername().setEditable(enable);;
		getNewPassword().setEditable(enable);;
		getNewPasswordConf().setEditable(enable);
		getEmail().setEditable(enable);
		getBtnLogin().setEnabled(enable);
		getBtnSignUp().setEnabled(enable);
		
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
		
		setNewUsername(new JTextField());
		getNewUsername().setBounds(415, 62, 126, 20);
		contentPane.add(getNewUsername());
		getNewUsername().setColumns(10);
		
		setNewPassword(new JPasswordField());
		getNewPassword().setBounds(415, 93, 126, 20);
		contentPane.add(getNewPassword());
		
		setNewPasswordConf(new JPasswordField());
		getNewPasswordConf().setBounds(415, 124, 126, 20);
		contentPane.add(getNewPasswordConf());
		
		setEmail(new JTextField());
		getEmail().setBounds(415, 155, 126, 20);
		contentPane.add(getEmail());
		getEmail().setColumns(10);
		
		setBtnLogin(new JButton("Login"));
		getBtnLogin().addActionListener(this);
		getBtnLogin().setFont(new Font("Calibri", Font.PLAIN, 13));
		getBtnLogin().setBounds(81, 154, 89, 23);
		contentPane.add(getBtnLogin());
		
		setBtnSignUp(new JButton("Sign Up"));
		getBtnSignUp().addActionListener(this);
		getBtnSignUp().setFont(new Font("Calibri", Font.PLAIN, 13));
		getBtnSignUp().setBounds(378, 184, 89, 23);
		contentPane.add(getBtnSignUp());
		
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
		
		setSignupError(new JLabel(""));
		getSignupError().setHorizontalAlignment(SwingConstants.CENTER);
		getSignupError().setFont(new Font("Calibri", Font.PLAIN, 13));
		getSignupError().setForeground(Color.RED);
		getSignupError().setBounds(306, 218, 238, 14);
		contentPane.add(getSignupError());
		
		setLoginProg(new JProgressBar());
		getLoginProg().setVisible(false);
		getLoginProg().setIndeterminate(true);
		getLoginProg().setBounds(81, 161, 89, 14);
		contentPane.add(getLoginProg());
		
		setSignupProg(new JProgressBar());
		getSignupProg().setVisible(false);
		getSignupProg().setIndeterminate(true);
		getSignupProg().setBounds(378, 193, 89, 14);
		contentPane.add(getSignupProg());
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

	public JButton getBtnSignUp() {
		return btnSignUp;
	}

	public void setBtnSignUp(JButton btnSignUp) {
		this.btnSignUp = btnSignUp;
	}

	public JProgressBar getSignupProg() {
		return signupProg;
	}

	public void setSignupProg(JProgressBar signupProg) {
		this.signupProg = signupProg;
	}

	public JLabel getSignupError() {
		return signupError;
	}

	public void setSignupError(JLabel signupError) {
		this.signupError = signupError;
	}

	public JTextField getNewUsername() {
		return newUsername;
	}

	public void setNewUsername(JTextField newUsername) {
		this.newUsername = newUsername;
	}

	public JPasswordField getNewPasswordConf() {
		return newPasswordConf;
	}

	public void setNewPasswordConf(JPasswordField newPasswordConf) {
		this.newPasswordConf = newPasswordConf;
	}

	public JPasswordField getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(JPasswordField newPassword) {
		this.newPassword = newPassword;
	}

	public JTextField getEmail() {
		return email;
	}

	public void setEmail(JTextField email) {
		this.email = email;
	}
}
