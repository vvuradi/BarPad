package com.example.barpad;

import java.util.Date;

import com.example.barpad.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CountActivity extends Activity {

	private int value;
	private long count;
	private boolean isSet;
	DataHandler dbHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		count = 0;
		value = 0;
		isSet = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		findViewById(R.id.send_to_server_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						insertCount();
					}
				});

		findViewById(R.id.reset_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resetCount();
					}
				});
	}

	/*public void onBackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}*/

	public void incCount(View view) {
		count += value;
		display();
		isSet = true;
		// value=0;
	}

	public void decCount(View view) {
		count -= value;
		display();
		isSet = true;
		// value=0;
	}

	private void display() {
		TextView c = ((TextView) findViewById(R.id.countView));
		c.setText(count + "");
	}

	public void getValue(View view) {
		int intID = view.getId();
		Button button = (Button) findViewById(intID);
		int number = Integer.parseInt(button.getText().toString());
		if (!isSet)
			value = (value * 10) + number;
		else {
			value = number;
			isSet = false;
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void insertCount() {
		System.out.println("Entered insert Count ");
		long countToSend = count;
		GlobalValues globalVar = (GlobalValues) getApplicationContext();
		String userName = globalVar.getUserName();
		Date d = new Date();
		dbHandler = new DataHandler(getBaseContext());
		dbHandler.open();
		long insertOutputClicker = dbHandler.insertClickerDetails(countToSend,
				userName, d.toString());
		System.out
				.println("Response from DB Clicker: ///////////////////////////////"
						+ insertOutputClicker);
		Toast.makeText(getBaseContext(), "Data Inserted", Toast.LENGTH_LONG)
				.show();
		dbHandler.close();

	}

	private void resetCount() {
		System.out.println("Entered reset Count ");
		count = 0;
		value = 0;
		display();
	}
}
