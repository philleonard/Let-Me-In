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

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

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

	@Override
	protected Object doInBackground(Object... arg0) {
		
		client = new Socket();
	
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.100.5", 8080);
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
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
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
		
		DataInputStream in = null;
		int size = 0;
		try {
			in = new DataInputStream(client.getInputStream());
			size = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		System.out.println("Size = " + size);
		ArrayList<String> name_list = new ArrayList<String>();
		
		for (int i = 0; i < size; i++) {
			
			Bitmap face = null;
			String name = null;
			String defaultAction = null;
			String group = null;
			
			try {
				name = in.readUTF();
				defaultAction = in.readUTF();
				group = in.readUTF();
				face = BitmapFactory.decodeStream(in);
			} catch (IOException e) {
				e.printStackTrace();
			}

			name_list.add(name);
			
			
			//Now set above data to each list item
		
		}
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return name_list;
	}
	
	private void populateList(Object result) {
		
		ArrayList<String> name_list = (ArrayList<String>) result;
		
		myLists.spin.setVisibility(View.INVISIBLE);
        // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(myLists,android.R.layout.simple_list_item_1, name_list);
		myLists.list.setAdapter(arrayAdapter);
		
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		populateList(result);	
	}

}