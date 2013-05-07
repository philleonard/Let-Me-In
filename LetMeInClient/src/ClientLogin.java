import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


public class ClientLogin extends Thread{
	
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
		
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.100.10", 8080);
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
