package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MyArrayAdapter extends ArrayAdapter<Item> {
	
	private ArrayList<Item> objects;
	MyLists myLists;
	public MyArrayAdapter (MyLists myLists, int listItemRes, ArrayList<Item> objects) {
		super(myLists, listItemRes, objects);
		this.objects = objects;
		this.myLists = myLists;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
				
				View v = convertView;

				if (v == null) {
					LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					v = inflater.inflate(R.layout.my_list_item, null);
				}

				final Item i = objects.get(position);
				
				final String[] actions = {"Open", "Keep out", "Notify me", "Undefined"};
				Spinner action = (Spinner) v.findViewById(R.id.spinner3);
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, actions);
				action.setAdapter(adapter);
				int j = 0;
				for (j = 0; j < actions.length; j++) {
					if (i.getAction().equals(actions[j]))
						break;
				}
				action.setSelection(j);
				action.setOnItemSelectedListener(new OnItemSelectedListener() {

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
				
				
				
				
				final String[] groups = {"Family", "Friends", "Colleagues", "Acquaintances", "Foe", "Undefined"};
				Spinner group = (Spinner) v.findViewById(R.id.spinner4);
				ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, groups);
				group.setAdapter(adapter1);
				int x = 0;
				for (x = 0; x < groups.length; x++) {
					if (i.getGroup().equals(groups[x]))
						break;
				}
				group.setSelection(x);
				group.setOnItemSelectedListener(new OnItemSelectedListener() {

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
				
				
				
				
				
				TextView name = (TextView) v.findViewById(R.id.name);
				ImageView face = (ImageView) v.findViewById(R.id.list_image);
				name.setText(i.getName());
				face.setImageBitmap(i.getFace());
	
				return v;
	}
}
