package com.barpad;

import com.barpad.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private UserLoginTask mAuthTask = null;
	DataHandler dbHandler;
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private int atCount;
	private int dotCount;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;

	// private TextView mLoginStatusMessageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.activity_login);
		setupActionBar();

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);

		/*
		 * findViewById(R.id.login_button).setOnClickListener( new
		 * View.OnClickListener() {
		 * 
		 * @Override public void onClick(View view) { validUser(); } });
		 */
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void validUser() {
		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		atCount = 0;
		dotCount = 0;

		if (mAuthTask != null) {
			return;
		}
		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() > 50) {
			mPasswordView.setError(getString(R.string.error_field_pwd_length));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else {

			// Check for only one occurence of '@' and '.'
			for (int i = 0; i < mEmail.length(); i++) {
				if (mEmail.charAt(i) == '@') {
					atCount++;
				}
				if (mEmail.charAt(i) == '.') {
					dotCount++;
				}
			}
			if (mEmail.length() > 100) {
				mEmailView
						.setError(getString(R.string.error_field_email_length));
				focusView = mEmailView;
				cancel = true;
			} else if (atCount > 1) {
				mEmailView.setError(getString(R.string.error_field_at_rate));
				focusView = mEmailView;
				cancel = true;
			} else if (atCount == 0) {
				mEmailView.setError(getString(R.string.error_invalid_email));
				focusView = mEmailView;
				cancel = true;
			} else if (dotCount == 0) { // Added code for . check
				mEmailView.setError(getString(R.string.error_invalid_email));
				focusView = mEmailView;
				cancel = true;
			}
		}
		if (cancel) {

			focusView.requestFocus();
		} else {

			// mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			dbHandler = new DataHandler(getBaseContext());
			dbHandler.open();
			String result = dbHandler.validateUser(mEmail, mPassword);
			System.out
					.println("Response from DB : ///////////////////////////////"
							+ result);
			if (result.equalsIgnoreCase("success")) {
				Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG)
						.show();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}

			dbHandler.close();
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);

		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {

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
			// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

}
