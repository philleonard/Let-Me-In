package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;

/*
 * After the user creates a new member in the AddNewToList activity. This AsyncTask is 
 * launched to send the information to the server so that the server can add the new 
 * item in the users list to the database.
 */

public class SendNew extends  AsyncTask<Object, Object, Object>{
	
	String name, groupStr, actionStr;
	Bitmap photo;
	Socket client;
	boolean accepted = false;
	AddNewToList addNewToList;
	String username;
	int error = 0;
	
	final int APP = 1;
	final int ADDLIST = 3;
	
	public SendNew(AddNewToList addNewToList, String name, String groupStr, String actionStr, Bitmap photo, String username) {
		this.name = name;
		this.groupStr = groupStr;
		this.actionStr = actionStr;
		this.photo = photo;
		this.addNewToList = addNewToList;
		this.username = username;
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
			error = 1;
			cancel(isCancelled());
		} catch (IOException e) {
			e.printStackTrace();
			error = 1;
			cancel(isCancelled());
		}
		try {
			client.setKeepAlive(true);
		} catch (SocketException e2) {
			error = 1;
			e2.printStackTrace();
		}
		
		DataOutputStream out;
		try {
			//Sending the new member information to the server
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(APP);
			out.writeInt(ADDLIST);
			out.writeUTF(username);
			out.writeUTF(name);
			out.writeUTF(groupStr);
			out.writeUTF(actionStr);
			
			//Image sent as byte array in PNG compressed format
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] photoByteArray = stream.toByteArray();
			out.writeInt(photoByteArray.length);
			out.write(photoByteArray);
			client.shutdownOutput();
		} catch (IOException e) {
			error = 1;
			e.printStackTrace();
			cancel(isCancelled());
		}
		
		//Retrieves a boolean for whether the new member was accepted or not.
		DataInputStream in;
		try {
			in = new DataInputStream(client.getInputStream());
			accepted = in.readBoolean();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
			error = 1;
			cancel(isCancelled());
		}
		
		return null;
	}
	
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		if (accepted) {
			//Once the new member has been added then reload the list activity to get the updated list as the list is not stored on the phone
			addNewToList.startActivity(new Intent(addNewToList.getApplicationContext(), MyLists.class));
			addNewToList.finish();
		}
		else {
			/*
			 * The server detects faces on the given photo and if none detected then it returns a false value so that 
			 * the user can then take the photo again
			 */
			
			addNewToList.errorText.setText("No faces detected in photo. Try again.");
		}
		resetVis();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		resetVis();
		if (error > 0) 
			addNewToList.errorText.setText("Error connectiong to server");
		else 
			addNewToList.errorText.setText("Unexpected error");
	}

	private void resetVis() {
		addNewToList.prog.setVisibility(View.INVISIBLE);
		addNewToList.addNew.setVisibility(View.VISIBLE);
		
	}

}
