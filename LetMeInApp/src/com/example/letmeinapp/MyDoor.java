package com.example.letmeinapp;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import com.example.facialrecogapp.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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