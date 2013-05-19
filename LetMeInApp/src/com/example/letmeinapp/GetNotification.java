package com.example.letmeinapp;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.Connection;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class GetNotification implements Runnable {

	Socket connection;
	HomeScreen homeScreen;
	
	public GetNotification(Socket connection, HomeScreen homeScreen) {
		this.connection = connection;
		this.homeScreen = homeScreen;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		DataInputStream in = null;
		String name;
		Bitmap face;
		try {
			in = new DataInputStream(connection.getInputStream());
			name = in.readUTF();
			face = BitmapFactory.decodeStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		

		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(homeScreen)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle(name + " is at your door")
		        .setLargeIcon(face)
		        .setContentText("Click here to respond");
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(homeScreen, AtTheDoor.class);
		resultIntent.putExtra("name", name);
		resultIntent.putExtra("face", face);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(homeScreen);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(HomeScreen.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager) homeScreen.getSystemService(Context.NOTIFICATION_SERVICE);
		int mId = 0;
		// mId allows you to update the notification later on.
		mNotificationManager.notify(mId , mBuilder.build());
		
		
	}

}