package com.example.letmeinapp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import android.os.AsyncTask;

public class MakeChanges extends AsyncTask<Object, Object, Object>{
	ArrayList<Item> itemList;
	
	final int APP = 1;
	final int CHANGE = 5;
	String uname;
	
	public MakeChanges(ArrayList<Item> itemList, String uname) {
		this.itemList = itemList;
		this.uname = uname;
	}

	Socket client;
	
	@Override
	protected Object doInBackground(Object... params) {
		
		client = new Socket();
		
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
		}
		
		DataOutputStream out = null;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(APP);
			out.writeInt(CHANGE);
			out.writeUTF(uname);
		} catch (IOException e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		for (int i = 0; i < itemList.size(); i++) {
			Item thisRow = itemList.get(i);
			try {
				out.writeUTF(thisRow.getName());
				out.writeUTF(thisRow.getAction());
				out.writeUTF(thisRow.getGroup());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
