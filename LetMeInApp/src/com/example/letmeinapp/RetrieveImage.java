package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/*
 * This retrieve image class downloads the webcam image directly from the client to show any activity at the users front door
 * at any time. By downloading the data straight from the client, it avoids overloading the server if many users where using this 
 * feature at the same time.
 */
public class RetrieveImage extends AsyncTask<Object, Object, Object> {
	
	ImageView webcamImage;
	ProgressBar load;
	TextView timeout;
	
	RetrieveImage(ImageView webcamImage, ProgressBar load, TextView timeout) {
		super();
		this.webcamImage = webcamImage;
		this.load = load;
		this.timeout = timeout;
	} 
	
	@Override
	protected Bitmap doInBackground(Object... arg0) {
		
    	Socket client = new Socket();

		GetAddress ga = new GetAddress();
		try {
			SocketAddress remoteAddr = new InetSocketAddress(ga.client(), ga.clientPort());
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
		
		//Setting the image view to the retrieved image
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