package com.example.letmeinapp;

import com.example.facialrecogapp.R;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Main extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		final Button loginButton = (Button) findViewById(R.id.login);
		final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.loginProgress);
		final EditText username = (EditText) findViewById(R.id.usename);
		final EditText password = (EditText) findViewById(R.id.password);
		final TextView error = (TextView) findViewById(R.id.failMessage);
		
		loginProgress.setVisibility(View.INVISIBLE);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loginButton.setVisibility(View.INVISIBLE);
				loginProgress.setVisibility(View.VISIBLE);
				error.setText("");
				
				String uname = username.getText().toString();
				String pass = password.getText().toString();
				
				AsyncTask<Object, Object, Object> li = new Login(uname, pass, error, loginProgress, loginButton, username, password, Main.this).execute();
				
			}
		});
	}	

}
