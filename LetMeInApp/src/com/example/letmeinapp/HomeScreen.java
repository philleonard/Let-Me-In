package com.example.letmeinapp;

import com.example.facialrecogapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class HomeScreen extends Activity {
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {

			super.onCreate(savedInstanceState);
			setContentView(R.layout.home_page);
			
			Button myDoorButton = (Button) findViewById(R.id.MyDoorButton);
			Button myListsButton = (Button) findViewById(R.id.MyListsButton);
			Button signoutButton = (Button) findViewById(R.id.SignoutButton);
			
			signoutButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), Main.class));
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
		}
		
}
