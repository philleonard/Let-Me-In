package com.example.letmeinapp;
/**
 * @author Philip Leonard
 * 
 */
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

/*
 * Class for retrieving notifications from the server. A connection is passed to this thread which then
 * reads in data from the server in order to then construct an informative notification about
 * who is at the users door.
 */
public class GetNotification implements Runnable {

	Socket connection;
	HomeScreen homeScreen;
	
	public GetNotification(Socket connection, HomeScreen homeScreen) {
		this.connection = connection;
		this.homeScreen = homeScreen;
	}

	@Override
	public void run() {
		//Reading the notifaction data in from the server.
		DataInputStream in = null;
		String name;
		Bitmap face;
		try {
			in = new DataInputStream(connection.getInputStream());
			//The name of the person at the door (unidentified if its a stranger)
			name = in.readUTF();
			//The image of the person at the door
			face = BitmapFactory.decodeStream(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
		//Building the notification for the user from the data. The notification is click-able and on a click it launches the AtTheDoor class
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(homeScreen).setSmallIcon(R.drawable.ic_launcher).setContentTitle(name + " is at your door").setLargeIcon(face).setContentText("Click here to respond");
		Intent resultIntent = new Intent(homeScreen, AtTheDoor.class);
		resultIntent.putExtra("name", name);
		resultIntent.putExtra("face", face);
		
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(homeScreen);
		stackBuilder.addParentStack(HomeScreen.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) homeScreen.getSystemService(Context.NOTIFICATION_SERVICE);
		int mId = 0;
		mNotificationManager.notify(mId , mBuilder.build());
		
		
	}

}
