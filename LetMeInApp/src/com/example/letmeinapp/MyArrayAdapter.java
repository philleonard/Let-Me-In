package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

/*
 * MyArrayAdapter class defines the layout and structure for each list item, 
 * i.e. the image, the name, the action of the buttons for each item
 */
public class MyArrayAdapter extends ArrayAdapter<Item> {
	
	private ArrayList<Item> objects;
	MyLists myLists;
	String username;
	public MyArrayAdapter (MyLists myLists, int listItemRes, ArrayList<Item> objects, String username) {
		super(myLists, listItemRes, objects);
		this.objects = objects;
		this.myLists = myLists;
		this.username = username;
	}
	
	/*Overrides typical getView method for Lists, so when the built in procedure for constructing 
	 *the list is called then it returns this view defined for each list item.
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
				
				View v = convertView;

				if (v == null) {
					LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inflater.inflate(R.layout.my_list_item, null);
				}

				final Item i = objects.get(position);
				
				//Populating action spinner
				final String[] actions = {"Open", "Keep out", "Notify me", "Undefined"};
				Spinner action = (Spinner) v.findViewById(R.id.spinner3);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, actions);
				action.setAdapter(adapter);
				int j = 0;
				for (j = 0; j < actions.length; j++) {
					if (i.getAction().equals(actions[j]))
						break;
					if (j == actions.length - 1)
						break;
				}
				action.setSelection(j);
				action.setOnItemSelectedListener(new OnItemSelectedListener() {

					//When the action type is selected then this updates the Item data
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						i.setAction(actions[arg2]);
						objects.set(position, i);
						myLists.setItemList(objects);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
					}
				});
				
				
				//Populating group spinner
				final String[] groups = {"Family", "Friends", "Colleagues", "Acquaintances", "Foe", "Undefined"};
				Spinner group = (Spinner) v.findViewById(R.id.spinner4);
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, groups);
				group.setAdapter(adapter1);
				int x = 0;
				for (x = 0; x < groups.length; x++) {
					if (i.getGroup().equals(groups[x]))
						break;
					if (x == groups.length - 1)
						break;
				}
				group.setSelection(x);
				group.setOnItemSelectedListener(new OnItemSelectedListener() {

					//When the group type is selected then this updates the Item data
					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
						i.setGroup(groups[arg2]);
						objects.set(position, i);
						myLists.setItemList(objects);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						
					}
				});
				
				//Sets a listener for the delete icon, when pressed launches a confirmation dialog
				ImageView deleteImage = (ImageView) v.findViewById(R.id.deleteImage);
				deleteImage.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						
						AlertDialog.Builder builder1 = new AlertDialog.Builder(myLists);
			            builder1.setMessage("Are you sure you want to delete " + i.getName() + "?");
			            builder1.setCancelable(true);
			            builder1.setPositiveButton("Yes",
			                    new DialogInterface.OnClickListener() {
			            	//If Yes is selected then start an AsyncTask thread to delete this item from the list, and from the server
			                public void onClick(DialogInterface dialog, int id) {
			                	AsyncTask<Object, Object, Object> dp = new DeletePerson(username, i.getName(), myLists).execute();
			                	myLists.startActivity(myLists.getIntent());
			            		myLists.finish();
			                    dialog.cancel();
			               
			                }
			            });
			            builder1.setNegativeButton("No",
			                    new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                    dialog.cancel();
			                }
			            });

			            AlertDialog alert11 = builder1.create();
			            alert11.show();
					}
				});
				

				TextView name = (TextView) v.findViewById(R.id.name);
				ImageView face = (ImageView) v.findViewById(R.id.list_image);
				name.setText(i.getName());
				face.setImageBitmap(i.getFace());
	
				return v;
	}
}
