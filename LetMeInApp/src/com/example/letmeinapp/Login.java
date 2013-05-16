package com.example.letmeinapp;
/*
 * @author Philip Leonard
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Login extends AsyncTask<Object, Object, Object>{
	
	final int APP = 1;
	final int LOGIN = 0;
	private static final String PREFS_NAME = "globalSettings";
	private SharedPreferences settings;
	String uname;
	String pass;
	TextView error;
	ProgressBar loginProgress;
	Button loginButton;
	Button signUpButton;
	Main main;
	EditText username;
	EditText password;
	int cancelReason = 0;
	Socket client;
	CheckBox remember;
	
	Login(String uname, String pass, TextView error, ProgressBar loginProgress, Button loginButton, EditText username, EditText password, Button signUpButton, CheckBox remember, Main main) {
		this.uname = uname;
		this.pass = pass;
		this.error = error;
		this.main = main;
		this.loginButton = loginButton;
		this.loginProgress = loginProgress;
		this.username = username;
		this.password = password;
		this.signUpButton = signUpButton;
		this.remember = remember;
	}
	
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
			cancelReason = 1;
			cancel(isCancelled());
		} catch (IOException e) {
			cancelReason = 9;
			e.printStackTrace();
		}
		
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(APP);
			out.writeInt(LOGIN);
			out.writeUTF(uname);
			out.writeUTF(pass);
		} catch (IOException e) {
			e.printStackTrace();
			cancelReason = 2;
			cancel(isCancelled());
		}
		
		DataInputStream in = null;
		try {
			in = new DataInputStream(client.getInputStream());
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
			e.printStackTrace();
		} 
		return null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		main.startActivity(new Intent(main.getApplicationContext(), HomeScreen.class));
		main.finish();
	}
	
	@Override
	protected void onCancelled() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		loginProgress.setVisibility(View.INVISIBLE);
		loginButton.setVisibility(View.VISIBLE);
		signUpButton.setVisibility(View.VISIBLE);
		
		if (cancelReason == 1)
			error.setText("Connection timeout");
		else if (cancelReason == 9)
			error.setText("Connection refused");
		else if (cancelReason == 7) {
			error.setText("User " + uname + " not found");
			username.setText("");
			password.setText("");
		}
		else if (cancelReason == 8) {
			error.setText("Incorrect password");
			password.setText("");
		}
		else
			error.setText("Server ERROR");
	}

}
