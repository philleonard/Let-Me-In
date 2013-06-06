package com.example.letmeinapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.os.AsyncTask;

/*
 * This AsyncTask thread sends a command to the server to delete a given name from
 * the users list.
 */
public class DeletePerson extends AsyncTask<Object, Object, Object>{

	Socket client;
	final int APP = 1;
	final int REMOVE = 4;
	
	String name, username;
	MyLists myLists;
	public DeletePerson(String username, String name, MyLists myLists) {
		this.username = username;
		this.name = name;
	}

	@Override
	protected Object doInBackground(Object... params) {
		Socket client = new Socket();
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
			cancel(isCancelled());
		}
		try {
			client.setKeepAlive(true);
		} catch (SocketException e2) {
			e2.printStackTrace();
		}
		
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(APP);
			out.writeInt(REMOVE);
			out.writeUTF(username);
			out.writeUTF(name);
		} catch (IOException e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		return null;
	}

}
