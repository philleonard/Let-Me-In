package com.example.letmeinapp;
/**
 * @author Philip Leonard
 * 
 */
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import android.os.AsyncTask;

/*
 * This Async Task sends a response to the server as to whether to allow or disallow a person through the door
 * from the AtTheDoor activity launched when the user clicks on a notification sent by the server.
 */
public class Respond extends AsyncTask<Object, Object, Object> {
	
	boolean letIn;
	private Socket client;
	String uname;
	int APP = 1;
	int RESPOND = 7;
	
	Respond(boolean letIn, String uname) {
		this.letIn = letIn;
		this.uname = uname;
	}
	@Override
	protected Object doInBackground(Object... params) {
		
		client = new Socket();
		GetAddress ga = new GetAddress();
		try {
			SocketAddress remoteAddr = new InetSocketAddress(ga.server(), ga.serverPort());
			client.connect(remoteAddr, 8000);
		} catch (SocketTimeoutException e) {
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			cancel(isCancelled());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(APP);
			out.writeInt(RESPOND);
			out.writeUTF(uname);
			
			//Send boolean value to open the door or keep it closed determined by button press in AtTheDoor activity
			out.writeBoolean(letIn);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		return null;
	}

}
