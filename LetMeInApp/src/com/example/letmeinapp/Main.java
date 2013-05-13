package com.example.letmeinapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class Main extends Activity {
	
	private SharedPreferences settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button loginButton = (Button) findViewById(R.id.login);
		final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
		final EditText username = (EditText) findViewById(R.id.username);
		final EditText password = (EditText) findViewById(R.id.password);
		final TextView error = (TextView) findViewById(R.id.userText);
		final Button signUpButton = (Button) findViewById(R.id.signUpButton);
		final CheckBox remember = (CheckBox) findViewById(R.id.remember);
		
		loadSaved(username, password);
		if (!username.getText().equals(null) && !password.getText().equals(null)) {
			remember.setChecked(true);
		}
		loginProgress.setVisibility(View.INVISIBLE);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (username.getText().toString().equals("") && password.getText().toString().equals(""))
					error.setText("Please enter a username and password");
				else if (username.getText().toString().equals(""))
					error.setText("Please enter a username");
				else if (password.getText().toString().equals(""))
					error.setText("Please enter your password");
				else {
					loginButton.setVisibility(View.INVISIBLE);
					loginProgress.setVisibility(View.VISIBLE);
					signUpButton.setVisibility(View.INVISIBLE);
					error.setText("");
					
					String uname = username.getText().toString();
					String pass = password.getText().toString();
					
					if (remember.isChecked())
						save(uname, pass);
					else
						save("","");
					
					AsyncTask<Object, Object, Object> li = new Login(uname, pass, error, loginProgress, loginButton, username, password, signUpButton, remember, Main.this).execute();
				}

			}
		});
		
		signUpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), SignUp.class));
				
			}
		});
	}
	
	private void save(String uname, String pass) {
		settings = getSharedPreferences("settings", MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("uname", uname);
		editor.putString("pass", pass);
		editor.commit();	
	}
	
	private void loadSaved(EditText username, EditText password) {
		settings = getSharedPreferences("settings", MODE_WORLD_READABLE);
		String user = null;
		String pass = null;
		username.setText(settings.getString("uname", user));
		password.setText(settings.getString("pass", pass));
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
}
