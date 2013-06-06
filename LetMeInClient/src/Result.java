import java.awt.Color;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Philip Leonard
 */
public class Result implements Runnable {

	/*
	 * This thread listens for a result from the server after the images have been sent for identification.
	 * The two results are to keep the door closed or to open it.
	 */
	
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
			e.printStackTrace();
		}
		
		Socket getResult = null;
		try {
			getResult = listen.accept();
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
			in = new DataInputStream(getResult.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientHome.getConsole().append(clientHome.console() + "Reading data from server.. \n");
		String[] nameArray;
		try {
			int size = in.readInt();
			String action = in.readUTF(); //open or phone
			nameArray = new String[size];
			
			String phoneOnline = "";
			phoneOnline = in.readUTF();
			
			//Receives the names of the people at the door
			String nameDisplay = "Recognised: ";
			for (int f = 0; f < size; f++) {
				if (f == size)
					nameDisplay = nameDisplay + in.readUTF() + ".";
				else
					nameDisplay = nameDisplay + in.readUTF() + ", ";
			}

			clientHome.getConsole().append(clientHome.console() + nameDisplay + "\n");
			
			clientHome.getFacesText().setText(nameDisplay);
			
			//If the received action is to open the door, then display so and then open the door
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
			
			//If the received action is that the phone is going to respond then wait for a response from the phone
			else if (action.equals("phone")) {
				if (phoneOnline.equals("online")) {
					clientHome.getConsole().append(clientHome.console() + "Server found face with 'notify me'. Waiting for phone & server response. \n");
					clientHome.getActionText().setText("STATUS: WAITING FOR RESPONSE ON PHONE...");
					Thread phoneResponse = new Thread(new PhoneResponse(clientHome));
					phoneResponse.start();
					phoneResponse.join();
				} 
				else {
					clientHome.getConsole().append(clientHome.console() + "Phone offline so no response. Keeping door closed\n");
				}
			}
			try {
				getResult.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}		
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
