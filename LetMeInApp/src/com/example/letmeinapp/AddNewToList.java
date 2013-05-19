package com.example.letmeinapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.FaceDetector;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * @author Philip Leonard
 */

public class AddNewToList extends Activity {
	protected static final int CAMERA_REQUEST = 1888;
	ImageView photoTaken;
	Bitmap photo;
	SharedPreferences settings;
	ProgressBar prog;
	TextView errorText;
	Button addNew; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_new);
		
		prog = (ProgressBar) findViewById(R.id.addNewProg);
		prog.setVisibility(View.INVISIBLE);
		photoTaken = (ImageView) findViewById(R.id.photo);
		final EditText nameText = (EditText) findViewById(R.id.nameText);
		Button takePhoto = (Button) findViewById(R.id.takePhoto);
		takePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
	                startActivityForResult(cameraIntent, CAMERA_REQUEST); 
				
			}
			
		});
		
		final String[] actions = {"Open", "Keep out", "Notify me", "Undefined"};
		final Spinner action = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, actions);
		action.setAdapter(adapter);
		action.setSelection(adapter.getCount() - 1);
		
		final String[] groups = {"Family", "Friends", "Colleagues", "Acquaintances", "Foe", "Undefined"};
		final Spinner group = (Spinner) findViewById(R.id.spinner4);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, groups);
		group.setAdapter(adapter1);
		group.setSelection(adapter1.getCount() - 1);
		
		errorText = (TextView) findViewById(R.id.errorText);
		errorText.setTextColor(Color.RED);
		addNew = (Button) findViewById(R.id.addNewButton);
		addNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				errorText.setText("");
				String name = nameText.getText().toString();
				String groupStr = group.getSelectedItem().toString();
				String actionStr = action.getSelectedItem().toString();
				settings = getSharedPreferences("settings", MODE_WORLD_READABLE);
				String username = settings.getString("uname", "");
				if (!(photo == null)) {
					prog.setVisibility(View.VISIBLE);
					addNew.setVisibility(View.INVISIBLE);
					AsyncTask<Object, Object, Object> sn = new SendNew(AddNewToList.this, name, groupStr, actionStr, photo, username).execute();
				}
				else {
					errorText.setText("No picture taken");
				}
			}
		});
	
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {  
            photo = (Bitmap) data.getExtras().get("data");
            photoTaken.setImageBitmap(photo);
        }  
    } 
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(getApplicationContext(), MyLists.class));
		finish();
	}
}
