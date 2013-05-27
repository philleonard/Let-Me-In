import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Philip Leonard
 */
public class Result implements Runnable {

	ClientHome clientHome;
	public Result(ClientHome clientHome) {
		this.clientHome = clientHome;
	}

	@Override
	public void run() {
		//Listen to result from server and wait for action
		clientHome.getConsole().append(clientHome.console() + "Waiting for server response... \n");
		
		ServerSocket listen = null;
		try {
			listen  = new ServerSocket(55555);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Socket getResult = null;
		try {
			getResult = listen.accept();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DataInputStream in = null;
		try {
			in = new DataInputStream(getResult.getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String[] nameArray;
		try {
			int size = in.readInt();
			String action = in.readUTF(); //open or phone
			nameArray = new String[size];
			
			String nameDisplay = "Recognised: ";
			for (int f = 0; f < size; f++) {
				nameDisplay = nameDisplay + in.readUTF() + ", ";
			}
			clientHome.getConsole().append(clientHome.console() + nameDisplay + "\n");
			
			clientHome.getFacesText().setText(nameDisplay);
			
			if (action.equals("open")) {
				clientHome.getConsole().append(clientHome.console() + "Opening the door... \n");
				clientHome.getActionText().setForeground(Color.GREEN);
				clientHome.getActionText().setText("STATUS: DOOR OPENED");
				Thread open = new Thread(new OpenDoor());
				open.start();
				open.join();
				clientHome.getActionText().setForeground(Color.RED);
				clientHome.getActionText().setText("STATUS: DOOR CLOSED");
				clientHome.getConsole().append(clientHome.console() + "Door has been closed \n");
			}
			
			else if (action.equals("phone")) {
				clientHome.getConsole().append(clientHome.console() + "Server found face with 'notify me'. Waiting for phone & server response. \n");
				clientHome.getActionText().setText("STATUS: WAITING FOR RESPONSE ON PHONE...");
				Thread phoneResponse = new Thread(new PhoneResponse());
				phoneResponse.start();
				phoneResponse.join();
			}
					
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
