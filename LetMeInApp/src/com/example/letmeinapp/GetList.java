package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;

/*
 * AsyncTask for downloading the users list from the server.
 * These include images, names, groups and actions for each profile
 */
public class GetList extends AsyncTask<Object, Object, Object> implements Serializable {
	
	private SharedPreferences settings;
	
	final int APP = 1;
	final int LIST = 2;
	MyLists myLists;
	String user;
	
	public GetList(String user, MyLists myLists) {
		this.user = user;
		this.myLists = myLists;
	}
	
	Socket client;
	DataInputStream in;
	
	@Override
	protected Object doInBackground(Object... arg0) {
		
		client = new Socket();
		GetAddress ga = new GetAddress();
		connect(ga.serverPort());
		
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(APP);
			out.writeInt(LIST);
			out.writeUTF(user);
		} catch (IOException e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		int size = 0;
		newDIS();
		try {
			size = in.readInt();
		} catch (Exception e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		ArrayList<Item> itemList = new ArrayList<Item>();
		
		//New non-persistent TCP connection started for profile
		for (int i = 0; i < size; i++) {
			
			connect(55556);
			newDIS();
		
			Bitmap face = null;
			String name = null;
			String defaultAction = null;
			String group = null;
			
			try {
				
				name = in.readUTF();
				defaultAction = in.readUTF();
				group = in.readUTF();
				face = BitmapFactory.decodeStream(in);
			
				itemList.add(new Item(name, group, defaultAction, face));
			} catch (IOException e) {
				e.printStackTrace();
				myLists.finish();
				cancel(isCancelled());
			}
			System.out.println(name);
			System.out.println(group);
			System.out.println(defaultAction);
		}
		
		MyArrayAdapter adapter = new MyArrayAdapter(myLists, R.layout.my_list_item, itemList, user);
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return adapter;
	}
	
	//Method for refreshing the data input stream
	private void newDIS() {
		in = null;
		try {
			in = new DataInputStream(client.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		
	}

	//Method for connecting to the server
	private void connect(int port) {
		client = new Socket();
		GetAddress ga = new GetAddress();
		try {
			SocketAddress remoteAddr = new InetSocketAddress(ga.server(), port);
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
		
	}

	//Method for adding data to list
	private void populateList(Object result) {
		
		MyArrayAdapter adapter = (MyArrayAdapter) result;
		myLists.spin.setVisibility(View.INVISIBLE);
		myLists.saveButton.setVisibility(View.VISIBLE);
		myLists.addNew.setVisibility(View.VISIBLE);
		myLists.list.setAdapter(adapter);
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		populateList(result);	
	}

}