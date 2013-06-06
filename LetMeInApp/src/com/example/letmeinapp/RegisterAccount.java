package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
 * This Async Task uploads the sign up information to the server. It then waits for the server to
 * respond either successful or unsuccessful (including a reason for error catching & handling).
 */
public class RegisterAccount extends AsyncTask<Object, Object, Object> {
	
	final int APP = 1;
	final int REGISTER = 1;
	int cancelReason = 0;
	int signupResponse = 0;
	String newUsername, password, passwordConfirm, emailAddress;
	EditText username, email;
	TextView errorText;
	SignUp signUpClass;
	ProgressBar load;
	Button signUp;
	Socket client;
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

		client = new Socket();
		GetAddress ga = new GetAddress();
		try {
			SocketAddress remoteAddr = new InetSocketAddress(ga.server(), ga.serverPort());
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
			out.writeInt(APP);
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
	
	/*
	 * If the doInBackground method returns then it starts this method, so the sign up has been successful
	 * and the app then returns to the login screen so that the user can sign in with their new account.
	 */
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		signUpClass.finish();
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//Error handling, displaying to the user why the sign up failed so that they can make adjustments to the information
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
