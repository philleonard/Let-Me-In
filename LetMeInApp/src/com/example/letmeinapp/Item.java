package com.example.letmeinapp;
/**
 * @author Philip Leonard
 */
import android.graphics.Bitmap;

//Class for defining the structure of an Item in the list in "MyLists"

public class Item {
	private Bitmap face;
	private String name;
	private String group;
	private String defaultAction;

	public Item(){

	}

	public Item(String name, String group, String defaultAction, Bitmap face){
		this.face = face;
		this.name = name;
		this.defaultAction = defaultAction;
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getAction() {
		return defaultAction;
	}

	public void setAction(String defaultAction) {
		this.defaultAction = defaultAction;
	}
	
	public Bitmap getFace() {
		return face;
	}

	public void setFace(Bitmap face) {
		this.face = face;
	}

}