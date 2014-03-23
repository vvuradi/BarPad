package com.example.helloworld;

import java.util.Date;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CountActivity extends Activity {

	private int value;
	private int count;
	private boolean isSet;
	DataHandler dbHandler;
	private UserLoginTask mAuthTask = null;
	private EditText mSyncView;
	/*
	 * private void nextActivity(){ Intent intent = new Intent(this,
	 * LoginActivity.class); startActivity(intent); }
	 */
	private GlobalValues globalVar;
	public static final String PREFS_NAME = "MyPrefsFile";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		count = settings.getInt("count", 0);
		// setSilent(silent);

		value = 0;
		isSet = false;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_count);
		display();
		findViewById(R.id.reset_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						resetCount();
					}
				});
	}

	@Override
	protected void onStop() {
		super.onStop();

		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("count", count);

		// Commit the edits!
		editor.commit();
	}

	public void incCount(View view) {
		count += value;
		display();
		isSet = true;
		insertCount();

		// value=0;
	}

	public void decCount(View view) {
		count -= value;
		display();
		isSet = true;

		insertCount();

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
		System.out.println("count to insert: " + count);
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
		// Toast.makeText(getBaseContext(), "Data Inserted",
		// Toast.LENGTH_LONG).show();
		dbHandler.close();
		mAuthTask = new UserLoginTask();
		mAuthTask.execute((Void) null);
	}

	private void resetCount() {
		System.out.println("Entered reset Count ");
		value = 0;
		count = 0;
		display();
	}

	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

			/*
			 * for (String credential : DUMMY_CREDENTIALS) { String[] pieces =
			 * credential.split(":"); if (pieces[0].equals(mEmail)) { // Account
			 * exists, return true if the password matches. return
			 * pieces[1].equals(mPassword); } }
			 */

			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;

			if (success) {
				// finish(); should go to next page
				// nextActivity();
			} else {
				mSyncView.setError(getString(R.string.error_error_sync));
				mSyncView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;

		}
	}
}
