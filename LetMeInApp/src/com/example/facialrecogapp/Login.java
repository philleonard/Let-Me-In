package com.example.facialrecogapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Login extends AsyncTask<Object, Object, Object>{
	
	String uname;
	String pass;
	TextView error;
	ProgressBar loginProgress;
	Button loginButton;
	Main main;
	int cancelReason = 0;
	
	Login(String uname, String pass, TextView error, ProgressBar loginProgress, Button loginButton, Main main) {
		this.uname = uname;
		this.pass = pass;
		this.error = error;
		this.main = main;
		this.loginButton = loginButton;
		this.loginProgress = loginProgress;
	}
	
	@Override
	protected Object doInBackground(Object... arg0) {
		
		Socket client = new Socket();
		
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.100.10", 8080);
			client.connect(remoteAddr, 8000);
		} catch (Exception e) {
			try {
				client.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
			cancelReason = 1;
			cancel(isCancelled());
		}
		
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(uname);
			out.writeUTF(pass);
		} catch (IOException e) {
			e.printStackTrace();
			cancelReason = 2;
			cancel(isCancelled());
		}
		
		ServerSocket responseServSocket = null;
		Socket responseSocket = null;
		try {
			responseServSocket = new ServerSocket(8080);
		} catch (IOException e1) {
			cancelReason = 3;
			e1.printStackTrace();
			cancel(isCancelled());
		}
		
		try {
			responseSocket = responseServSocket.accept();
		} catch (IOException e1) {
			cancelReason = 4;
			e1.printStackTrace();
			cancel(isCancelled());
		}
		DataInputStream in = null;
		try {
			in = new DataInputStream(responseSocket.getInputStream());
		} catch (Exception e) {
			e.printStackTrace();
			cancelReason = 5;
			cancel(isCancelled());
		}
		
		int loginResponse = 0;
		try {
			loginResponse = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			cancelReason = 6;
			cancel(isCancelled());
		}
		System.out.println("Response code is: " + loginResponse);
		if (loginResponse == 1) {
			cancelReason = 7;
			cancel(isCancelled());
		}
		
		else if (loginResponse == 2) {
			cancelReason = 8;
			cancel(isCancelled());
		}
		
		try {
			client.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		main.startActivity(new Intent(main.getApplicationContext(), HomeScreen.class));
	}
	
	@Override
	protected void onCancelled() {
		System.out.println("CANCELLED");
		loginProgress.setVisibility(View.INVISIBLE);
		loginButton.setVisibility(View.VISIBLE);
		if (cancelReason == 1)
			error.setText("Connection timeout...");
		else if (cancelReason == 7)
			error.setText("User " + uname + " not found");
		else if (cancelReason == 8)
			error.setText("Incorrect password");
	}

}
