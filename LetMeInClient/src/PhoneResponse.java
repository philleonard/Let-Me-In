import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Philip Leonard
 */
public class PhoneResponse implements Runnable {

	/*
	 * This thread listens for a connection from the server regarding the phones response
	 * to a "notify me" action from the server regarding a detected face image sent from the client 
	 */
	
	boolean open;
	ClientHome clientHome;
	
	public PhoneResponse(ClientHome clientHome) {
		this.clientHome = clientHome;
	}
	@Override
	public void run() {
		
		//Listen for response from the server
		
		ServerSocket listen = null;
		try {
			listen  = new ServerSocket(55556);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Socket lisAccept = null;
		try {
			lisAccept = listen.accept();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			listen.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		DataInputStream in = null;
		try {
			in = new DataInputStream(lisAccept.getInputStream());
			open = in.readBoolean();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (open) {
			//If the open boolean is true then open the door and display it doing so
			clientHome.getConsole().append(clientHome.console() + "Phone responded, Opening the door... \n");
			clientHome.getActionText().setForeground(Color.GREEN);
			clientHome.getActionText().setText("STATUS: DOOR OPENED");
			Thread open = new Thread(new OpenDoor());
			open.start();
			try {
				open.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clientHome.getActionText().setForeground(Color.RED);
			clientHome.getActionText().setText("STATUS: DOOR CLOSED");
			clientHome.getConsole().append(clientHome.console() + "Door has been closed \n");
		}
		else {
			//Keep closed & notify
			clientHome.getConsole().append(clientHome.console() + "Phone responded, keeping door closed... \n");
			clientHome.getActionText().setText("STATUS: DOOR CLOSED");
		}
		try {
			lisAccept.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}
