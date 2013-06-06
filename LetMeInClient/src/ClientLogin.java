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

public class ClientLogin extends Thread{
	
	/*
	 * This thread manages logging into an account, the user entered username and password are passed here
	 * and are uploaded to the server, which gives a response as to whether they are correct.
	 */
	
	final int CLIENT = 0;
	final int LOGIN = 0;
	final int REGISTER = 1;

	String uname, pass;
	ClientMain clientMain;
	Socket client;
	
	public ClientLogin(String uname, String pass, ClientMain clientMain) {
		this.uname = uname;
		this.pass = pass;
		this.clientMain = clientMain;
	}

	@Override
	public void run() {
		
		client = new Socket();
		
		//Connecting to the server
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.1.178", 55555);
			client.connect(remoteAddr, 8000);
		} catch (SocketTimeoutException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Connection timeout");
			resetVis();
			return;
		} catch (IOException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Connection refused");
			resetVis();
			return;
		}
		
		//Writing login details
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(CLIENT);
			out.writeInt(LOGIN);
			out.writeUTF(uname);
			out.writeUTF(pass);
		} catch (IOException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Unexpected Error Loging in");
			resetVis();
			return;
		}
		
		DataInputStream in = null;
		try {
			in = new DataInputStream(client.getInputStream());
		} catch (Exception e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Unexpected Error Loging in");
			resetVis();
			return;
		}
		
		//Receive response code
		int loginResponse = 0;
		try {
			loginResponse = in.readInt();
		} catch (IOException e) {
			closeSocket();
			e.printStackTrace();
			clientMain.getLoginError().setText("Unexpected Error Loging in");
			resetVis();
			return;
		}
		
		closeSocket();
		
		//Response code handling 
		if (loginResponse == 1) {
			clientMain.getLoginError().setText("User " + uname + " not found");
			resetVis();
			return;
		}
		
		else if (loginResponse == 2) {
			clientMain.getLoginError().setText("Incorrect password");
			resetVis();
			return;
		}
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		clientMain.dispose();
		ClientHome ch = new ClientHome(uname);
		ch.setVisible(true);
	}
	
	public void resetVis() {
		clientMain.getBtnLogin().setVisible(true);
		clientMain.getLoginProg().setVisible(false);
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
