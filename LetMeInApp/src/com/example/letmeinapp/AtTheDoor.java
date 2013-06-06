package com.example.letmeinapp;
/**
 * @author Philip leonard
 */
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

/*When a notification is pressed then it launches this activity 
 * which displays the photo of the person taken by the webcam
 * and allows the user to decide the action to take
 * (i.e. open or keep the door closed)
 */
public class AtTheDoor extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.at_the_door);
		Bundle bundle = getIntent().getExtras();
		
		String name = bundle.getString("name");
		Bitmap face = (Bitmap) bundle.get("face");
		
		ImageView faceImage = (ImageView) findViewById(R.id.atDoorFace);
		faceImage.setImageBitmap(face);
		
		Button open = (Button) findViewById(R.id.openDoor);
		open.setText("Open door for " + name);
		open.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Clicking open starts AsyncTask thread which sends a command to the server then to the client to open the door
				AsyncTask<Object, Object, Object> res = new Respond(true, getUname()).execute();
				startActivity(new Intent(getApplicationContext(), HomeScreen.class));
				finish();
			}
		});
		
		Button keepOut = (Button) findViewById(R.id.refuseEntry);
		keepOut.setText("Keep " + name + " out");
		keepOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Clicking close starts AsyncTask thread which sends a command to the server then to the client to keep the door closed
				AsyncTask<Object, Object, Object> res = new Respond(false, getUname()).execute();
				startActivity(new Intent(getApplicationContext(), HomeScreen.class));
				finish();
			}
		});
		
	}
	
	public String getUname() {
		SharedPreferences settings = getSharedPreferences("settings", MODE_WORLD_READABLE);
		return settings.getString("uname", "uname");
		
	}
}
