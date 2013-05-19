package com.example.letmeinapp;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.imageio.ImageIO;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
/**
 * @author Philip Leonard
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
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(APP);
			out.writeInt(ADDLIST);
			out.writeUTF(username);
			out.writeUTF(name);
			out.writeUTF(groupStr);
			out.writeUTF(actionStr);
			
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
			addNewToList.startActivity(new Intent(addNewToList.getApplicationContext(), MyLists.class));
			addNewToList.finish();
		}
		else {
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
