package com.example.letmeinapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.content.Intent;
import android.os.AsyncTask;

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
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.100.6", 55555);
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
