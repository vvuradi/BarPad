package com.barpad;

import com.barpad.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GlobalValues globalVar = (GlobalValues) getApplicationContext();
		if (!globalVar.isLoggedIn()) {
			setContentView(R.layout.activity_main);
		} else {
			Intent intent = new Intent(this, CountActivity.class);
			startActivity(intent);
			finish();
		}
		setContentView(R.layout.activity_main);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void registerUser(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
	/*
	 * private void registerOrLoad(){ GlobalValues globalVar = (GlobalValues)
	 * getApplicationContext(); if(globalVar.isLoggedIn()){
	 * 
	 * }else{ Intent intent = new Intent(this, RegisterActivity.class);
	 * startActivity(intent); } }
	 */

}
