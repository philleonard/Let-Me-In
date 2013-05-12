package com.example.letmeinapp;

import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class RetreiveImage extends AsyncTask<Object, Object, Object> {
	
	ImageView webcamImage;
	ProgressBar load;
	TextView timeout;
	
	RetreiveImage(ImageView webcamImage, ProgressBar load, TextView timeout) {
		super();
		this.webcamImage = webcamImage;
		this.load = load;
		this.timeout = timeout;
	} 
	
	@Override
	protected Bitmap doInBackground(Object... arg0) {
		
    	Socket client = new Socket();
		BufferedImage webcamPhoto = null;
		
		try {
			SocketAddress remoteAddr = new InetSocketAddress("192.168.1.73", 8100);
			client.connect(remoteAddr , 5000);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Failed To connect");
			cancel(isCancelled());
	   	} 
		
		DataInputStream in;
		Bitmap webcamBmp = null;
		try {
			in = new DataInputStream(client.getInputStream());
			webcamBmp = BitmapFactory.decodeStream(in);
		} catch (IOException e) {
			System.out.println("Failed to get image from datastream");
			cancel(isCancelled());
		}
		return webcamBmp;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		super.onPostExecute(result);
		Bitmap bm = (Bitmap) result;
		load.setVisibility(View.INVISIBLE);
		timeout.setVisibility(View.INVISIBLE);
        webcamImage.setImageBitmap(bm);
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		System.out.println("CANCELLED");
		load.setVisibility(View.VISIBLE);
		timeout.setVisibility(View.VISIBLE);
	}
	
}