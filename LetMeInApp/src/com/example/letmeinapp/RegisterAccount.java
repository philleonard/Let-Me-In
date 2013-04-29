package com.example.letmeinapp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RegisterAccount extends AsyncTask<Object, Object, Object> {
	
	final int REGISTER = 1;
	int cancelReason = 0;
	int signupResponse = 0;
	String newUsername, password, passwordConfirm, emailAddress;
	EditText username, email;
	TextView errorText;
	SignUp signUpClass;
	ProgressBar load;
	Button signUp;
	
	public RegisterAccount(String newUsername, String password, String emailAddress, EditText username, EditText email, TextView errorText, ProgressBar load, Button signUp, SignUp signUpClass) {
		this.newUsername = newUsername;
		this.password = password;
		this.emailAddress = emailAddress;
		this.username = username;
		this.email = email;
		this.errorText = errorText;
		this.signUpClass = signUpClass;
		this.load = load;
		this.signUp = signUp;
	}

	@Override
	protected Object doInBackground(Object... params) {

		Socket client = new Socket();
		
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.100.10", 8080);
			client.connect(remoteAddr, 8000);
		} catch (Exception e) {
			try {
				client.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			cancelReason = 1;
			cancel(isCancelled());
		}
		
		DataOutputStream out;
		try {
			out = new DataOutputStream(client.getOutputStream());
			out.writeInt(REGISTER);
			out.writeUTF(newUsername);
			out.writeUTF(password);
			out.writeUTF(emailAddress);
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
		
		
		try {
			signupResponse = in.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			cancelReason = 6;
			cancel(isCancelled());
		}
		
		if (signupResponse > 0)
			cancel(isCancelled());
		
		return signupResponse;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		signUpClass.finish();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		if (cancelReason == 1)
			errorText.setText("Connection timeout...");
		if (cancelReason > 1)
			errorText.setText("Error signing up, try again");
		if (signupResponse == 1)
			errorText.setText("Username is already in use");
		if (signupResponse == 2)
			errorText.setText("Email is already in use");
		if (signupResponse == 3)
			errorText.setText("Username and email in use");
		signUp.setVisibility(View.VISIBLE);
		load.setVisibility(View.INVISIBLE);
	}

}
