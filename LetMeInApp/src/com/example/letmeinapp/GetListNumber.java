package com.example.letmeinapp;
/*
 * @author Philip Leonard
 */
//import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class GetListNumber extends AsyncTask<Object, Object, Object> implements Serializable {
	
	private SharedPreferences settings;
	
	final int APP = 1;
	final int LIST = 2;
	MyLists myLists;
	String user;
	
	public GetListNumber(String user, MyLists myLists) {
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
		
		ObjectInputStream objIn = null;
		try {
			objIn = new ObjectInputStream(client.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		
		MyObject listObject = null;
		try {
			 listObject = (MyObject) objIn.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			cancel(isCancelled());
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cancel(isCancelled());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		return listObject;
	}
	
	private void populateList(Object listObject) {
		
		
		Object[] listObjectArray = (Object[]) listObject;
		
		ArrayList<String> name_list = new ArrayList<String>();
		
		for (int i = 0; i < listObjectArray.length; i++) {
			Object[] itemObjectArray = (Object[]) listObjectArray[i];
			
			//BufferedImage face = (BufferedImage) itemObjectArray[0];
			String personName = (String) itemObjectArray[1];
			name_list.add(personName);
			String automaticResponse = (String) itemObjectArray[2];
			String group = (String) itemObjectArray[3];
			
			//Now set above data to each list item
		
		}
		
        // This is the array adapter, it takes the context of the activity as a first // parameter, the type of list view as a second parameter and your array as a third parameter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(myLists,android.R.layout.simple_list_item_1, name_list);
		myLists.list.setAdapter(arrayAdapter);
		myLists.spin.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		populateList(result);	
	}

}

class MyObject implements Serializable {
	Object[] objectListArray;
}
