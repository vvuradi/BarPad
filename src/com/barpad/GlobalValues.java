package com.barpad;

import android.app.Application;

public class GlobalValues extends Application {
	private String userName;
	private boolean loggedIn;
	private int saveCount = 0; // to save count state when home button pressed

	public int getSaveCount() {
		return saveCount;
	}

	public void setSaveCount(int saveCount) {
		this.saveCount = saveCount;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
