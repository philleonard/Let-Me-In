package com.example.letmeinapp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

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
		
		while(true) {
			try {
				connection = listen.accept();
				Thread getNot = new Thread(new GetNotification(connection, homeScreen));
				getNot.start();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
