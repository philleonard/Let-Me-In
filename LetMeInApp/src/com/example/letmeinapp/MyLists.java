package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

/*
 * The my lists activity retrieves and displays the list of people who the user has configured to allow
 * disallow or notify about when they are at the door (monitored by the client). This class also allows the addition
 * and deletion of members from the list. 
 */
public class MyLists extends Activity {
	
	ArrayList<Item> itemList;
	
	SharedPreferences settings;
	ListView list;
	ProgressBar spin;
	Button saveButton, addNew;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_lists);
		
		list = (ListView) findViewById(R.id.listView1);		
		spin = (ProgressBar) findViewById(R.id.myListProg);
		
		AsyncTask<Object, Object, Object> ln = new GetList(loadSaved(), this).execute();
		
		addNew = (Button) findViewById(R.id.addButton);
		addNew.setVisibility(View.INVISIBLE);
		addNew.setOnClickListener(new OnClickListener() {
			
			//Button which launches the add new function of the application to allow the user to add a new member to the list from their phone
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(), AddNewToList.class));
				finish();
				
			}
		});
		
		saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setVisibility(View.INVISIBLE);
		saveButton.setOnClickListener(new OnClickListener() {
			
			//Save button uploads changes to the list to the server (lists are not stored on the phone).
			@Override
			public void onClick(View v) {
				AsyncTask<Object, Object, Object> mc = new MakeChanges(itemList, loadSaved()).execute();			
				finish();
			}
		});
	}
	
	private String loadSaved() {
		settings = getSharedPreferences("settings", MODE_WORLD_READABLE);
		String user = "";
		return settings.getString("uname", user);
	}

	public void setItemList(ArrayList<Item> objects) {
		itemList = objects;
		
	} 
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	
}
