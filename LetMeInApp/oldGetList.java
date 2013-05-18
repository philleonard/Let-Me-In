package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
//import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;

public class GetListOld extends AsyncTask<Object, Object, Object> implements Serializable {
	
	private SharedPreferences settings;
	
	final int APP = 1;
	final int LIST = 2;
	MyLists myLists;
	String user;
	
	public GetListOld(String user, MyLists myLists) {
		this.user = user;
		this.myLists = myLists;
	}
	

	Socket client;
	DataInputStream in = null;
	
	@Override
	protected Object doInBackground(Object... arg0) {
		
		client = new Socket();
	
		connect();
		
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
		
		for (int i = 0; i < size; i++) {
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
			connect();
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
		
		MyArrayAdapter adapter = new MyArrayAdapter(myLists, R.layout.my_list_item, itemList);
		
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return adapter;
	}
	
	private void newDIS() {
		try {
			in.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		in = null;
		try {
			in = new DataInputStream(client.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		
	}

	private void connect() {
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.100.6", 8080);
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

	private void populateList(Object result) {
		
		MyArrayAdapter adapter = (MyArrayAdapter) result;
		myLists.spin.setVisibility(View.INVISIBLE);
	
		myLists.list.setAdapter(adapter);
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		populateList(result);	
	}

}