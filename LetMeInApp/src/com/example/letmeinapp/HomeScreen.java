package com.example.letmeinapp;
/*
 * @author Philip Leonard
 */
import com.example.letmeinapp.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class HomeScreen extends Activity {
	
		private SharedPreferences settings;

		@Override
		protected void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.home_page);
			
			Button myDoorButton = (Button) findViewById(R.id.MyDoorButton);
			Button myListsButton = (Button) findViewById(R.id.MyListsButton);
			Button signoutButton = (Button) findViewById(R.id.SignoutButton);
			TextView userText = (TextView) findViewById(R.id.userText);
			setUser(userText);
			signoutButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), Main.class));
					finish();
				}
			});
			
			myDoorButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), MyDoor.class));
				}
			});
			
			myListsButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), MyLists.class));
				}
			});
		}
		
		@Override
		public void onBackPressed() {
			super.onBackPressed();
			startActivity(new Intent(getApplicationContext(), Main.class));
			finish();
		}
		
		public void setUser(TextView userText) {
			settings = getSharedPreferences("settings", MODE_WORLD_READABLE);
			String user = "";
			userText.setText("Signed in as: " + settings.getString("uname", user));
		}
		
}
