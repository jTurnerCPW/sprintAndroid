package com.example.sprint;

public class Printer {
	
	private String mName;
	private String mLocation;
	private boolean mColor;
	
	public Printer(String name, String location, boolean color){
		mName = name;
		mLocation = location;
		mColor = color;
	}
	
	public void setColor(boolean color){mColor = color;}
	public void setLocation(String location){mLocation = location;}
	public void setName(String name){mName = name;}
	
	public boolean getColor(){return mColor;}
	public String getLocation(){return mLocation;}
	public String getName(){return mName;}

}