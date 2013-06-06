package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/*My Door activity displays the live image from the clients webcam
 *so that the user can view any activity happening at their door. 
 */
public class MyDoor extends Activity {
	ProgressBar load;
	ImageView webcamImage;
	TextView timeout;
	AsyncTask<Object, Object, Object> ri;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_door);
		
		load = (ProgressBar) findViewById(R.id.ImageLoad);
		webcamImage = (ImageView) findViewById(R.id.webcamImage);
		timeout = (TextView) findViewById(R.id.timeouttext);
		timeout.setVisibility(View.INVISIBLE);
		
		//Timer schedule gets image from client every 5 seconds by starting a new thread 
		new Timer().schedule(new TimerTask() {
			public void run() {
			    runOnUiThread(new Runnable() {
	
					@Override
					public void run() {
						ri = new RetrieveImage(webcamImage, load, timeout).execute();
					}
			    	
			    });
			}
		}, 5000, 5000);
	}
	
	public void onBackPressed() {
		finish();
	}
			
}