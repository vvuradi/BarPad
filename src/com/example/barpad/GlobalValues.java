package com.example.barpad;

import android.app.Application;

public class GlobalValues extends Application{
	private String userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	} 
}
