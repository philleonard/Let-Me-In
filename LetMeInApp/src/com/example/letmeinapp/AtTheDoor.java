package com.example.letmeinapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

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
				// TODO Auto-generated method stub
				
			}
		});
		
		Button keepOut = (Button) findViewById(R.id.refuseEntry);
		keepOut.setText("Keep " + name + " out");
		keepOut.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
}
