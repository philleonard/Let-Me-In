package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SignUp extends Activity {
	
	String newUsername, password, passwordConfirm, emailAddress = "";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);
		
		final Button signUp = (Button) findViewById(R.id.signUp);
		final EditText username = (EditText) findViewById(R.id.newUsername);
		final EditText pass = (EditText) findViewById(R.id.newPass);
		final EditText passConf = (EditText) findViewById(R.id.newPassConf);
		final TextView errorText = (TextView) findViewById(R.id.signUpErrorText);
		final EditText email = (EditText) findViewById(R.id.newEmail);
		final ProgressBar load = (ProgressBar) findViewById(R.id.signupload);
		
		load.setVisibility(View.INVISIBLE);
		
		signUp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!verifyForm(errorText, username, pass, passConf, email))
					return;
				signUp.setVisibility(View.INVISIBLE);
				load.setVisibility(View.VISIBLE);
				
				AsyncTask<Object, Object, Object> ra = new RegisterAccount(newUsername, password, emailAddress, username, email, errorText, load, signUp, SignUp.this).execute();
			}
		});
 	}
	
	public boolean verifyForm(TextView errorText2, EditText username2, EditText pass2, EditText passConf2, EditText email2) {
		int emptyNumber = 0;
		errorText2.setText("");
		
		if ((newUsername = username2.getText().toString()).equals("")) {
			errorText2.setText("Username field empty");
			emptyNumber++;
		}
		
		if ((password = pass2.getText().toString()).equals("")) {
			errorText2.setText("Password field empty");
			emptyNumber++;
		}
		
		if ((passwordConfirm = passConf2.getText().toString()).equals("")) {
			errorText2.setText("Password confirm field empty");
			emptyNumber++;
		}
		
		if ((emailAddress = email2.getText().toString()).equals("")) {
			errorText2.setText("Email field empty");
			emptyNumber++;
		}
		
		if (emptyNumber == 1) {
			return false;
		}
		
		if (emptyNumber > 1) {
			errorText2.setText("Multiple fields are empty");
			return false;
		}
		
		if (!password.equals(passwordConfirm)) {
			errorText2.setText("Passwords do not match");
			return false;
		}
		
		return true;
	}

}
