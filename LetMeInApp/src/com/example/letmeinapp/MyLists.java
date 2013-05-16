package com.example.letmeinapp;
/*
 * @author Philip Leonard
 */
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import com.example.letmeinapp.R;

import android.app.Activity;
import android.app.LauncherActivity.ListItem;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MyLists extends Activity {
	
	SharedPreferences settings;
	ListView list;
	ProgressBar spin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_lists);
		
		list = (ListView) findViewById(R.id.listView1);		
		spin = (ProgressBar) findViewById(R.id.myListProg);
		
		AsyncTask<Object, Object, Object> ln = new GetList(loadSaved(), this).execute();
		
	}
	
	private String loadSaved() {
		settings = getSharedPreferences("settings", MODE_WORLD_READABLE);
		String user = "";
		return settings.getString("uname", user);
	} 
	
	
}
