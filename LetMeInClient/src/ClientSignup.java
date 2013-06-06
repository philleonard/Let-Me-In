import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
/**
 * @author Philip Leonard
 */

public class ClientSignup extends Thread{
	
	/*
	 * Thread for uploading signup details for a new account. The server responds as to whether
	 * the new account has been accepted (and created) or not.
	 */
	final int CLIENT = 0;
	final int LOGIN = 0;
	final int REGISTER = 1;

	String uname, pass;
	ClientMain clientMain;
	String email;
	int signupResponse = 0;
	Socket client;
	
	//Username, password and email all required
	public ClientSignup(String uname, String pass, String email, ClientMain clientMain) {
		this.uname = uname;
		this.pass = pass;
		this.email = email;
		this.clientMain = clientMain;
	}

	@Override
	public void run() {
		
		clientMain.getSignupError().setText("");
		client = new Socket();
		
		//Connection to server
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.1.178", 8080);
			client.connect(remoteAddr, 8000);
		} catch (SocketTimeoutException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getSignupError().setText("Connection timeout");
			resetVis();
			return;
		} catch (IOException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getSignupError().setText("Connection refused");
			resetVis();
			return;
		}
		
		//Writing signup details
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(CLIENT);
			out.writeInt(REGISTER);
			out.writeUTF(uname);
			out.writeUTF(pass);
			out.writeUTF(email);
		} catch (IOException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Unexpected Error Signing up");
			resetVis();
			return;
		}
		
		DataInputStream in = null;
		try {
			in = new DataInputStream(client.getInputStream());
		} catch (Exception e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Unexpected Error Signing up");
			resetVis();
			return;
		}
		
		//Reading response from server
		try {
			signupResponse = in.readInt();
		} catch (IOException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Unexpected Error Signing up");
			resetVis();
			return;
		}
		
		closeSocket();
		
		//Handing response
		if (signupResponse == 1) {
			clientMain.getSignupError().setText("Username is already in use");
			resetVis();
			return;
		}
		if (signupResponse == 2) {
			clientMain.getSignupError().setText("Email is already in use");
			resetVis();
			return;
		}
		if (signupResponse == 3) {
			clientMain.getSignupError().setText("Username and email in use");
			resetVis();
			return;
		}
		
		//Reseting forms in clientMain GUI
		clientMain.getNewUsername().setText("");
		clientMain.getNewPassword().setText("");
		clientMain.getNewPasswordConf().setText("");
		clientMain.getEmail().setText("");
		
		clientMain.getSignupError().setForeground(Color.green);
		clientMain.getSignupError().setText("Account " + uname + " created");
		resetVis();
	}
	
	public void resetVis() {
		clientMain.getBtnSignUp().setVisible(true);
		clientMain.getSignupProg().setVisible(false);
		clientMain.enableComponents(true);
	}
	
	public void closeSocket() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
