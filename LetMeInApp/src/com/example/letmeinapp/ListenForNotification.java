package com.example.letmeinapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/*
 * If someone is at the users door and their face is recognised and the default action for that 
 * person is to notify the user on their phone then the server sends a notification.
 * This class listens for a connection from the server.
 */
public class ListenForNotification implements Runnable {

	ServerSocket listen;
	Socket connection;
	HomeScreen homeScreen;
	
	public ListenForNotification(HomeScreen homeScreen) {
		this.homeScreen = homeScreen;
	}
	
	@Override
	public void run() {
		try {
			listen = new ServerSocket(55557);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			listen.setSoTimeout(0);
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Listening and when it accepts it launches a thread to retrieve the notification information.
		while(true) {
			try {
				connection = listen.accept();
				Thread getNot = new Thread(new GetNotification(connection, homeScreen));
				getNot.start();
				listen.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
	}

}
