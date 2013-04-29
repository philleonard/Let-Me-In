package com.example.letmeinapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyDoor extends Activity {
	ProgressBar load;
	ImageView webcamImage;
	TextView timeout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_door);
		
		load = (ProgressBar) findViewById(R.id.ImageLoad);
		webcamImage = (ImageView) findViewById(R.id.webcamImage);
		timeout = (TextView) findViewById(R.id.timeouttext);
		timeout.setVisibility(View.INVISIBLE);
		
		new Timer().schedule(new TimerTask() {
			public void run() {
			    runOnUiThread(new Runnable() {
	
					@Override
					public void run() {
						AsyncTask<Object, Object, Object> ri = new RetreiveImage(webcamImage, load, timeout).execute();
					}
			    	
			    });
			}
		}, 5000, 5000);
	}
	
	public void onBackPressed() {
		finish();
	}
			
}