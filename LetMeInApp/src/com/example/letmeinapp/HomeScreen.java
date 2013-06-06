package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/*
 * This class is launched when the user logs in. It starts to listen for notifications from the server and it also
 * allows the user to navigate to the rest of the application
 */
public class HomeScreen extends Activity {
	
		private SharedPreferences settings;

		@Override
		protected void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.home_page);
			
			Thread serverListen = new Thread(new ListenForNotification(this));
			serverListen.start();
			
			Button myDoorButton = (Button) findViewById(R.id.MyDoorButton);
			Button myListsButton = (Button) findViewById(R.id.MyListsButton);
			Button signoutButton = (Button) findViewById(R.id.SignoutButton);
			TextView userText = (TextView) findViewById(R.id.userText);
			setUser(userText);
			signoutButton.setOnClickListener(new OnClickListener() {
				
				//Sign out button returns to the login screen
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), Main.class));
					finish();
				}
			});
			
			myDoorButton.setOnClickListener(new OnClickListener() {
				
				//My door button starts the my door activity which retrieves displays the webcam image of the client.
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), MyDoor.class));
				}
			});
			
			myListsButton.setOnClickListener(new OnClickListener() {
				
				//My lists button launches the my lists activity which displays the users permitted people
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), MyLists.class));
				}
			});
		}

		@Override
		public void onBackPressed() {
			//Back pressed also signs out of the application and returns to the login screen
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
