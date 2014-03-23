package com.barpad;

import com.barpad.R;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class RegisterActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	// private static final String[] DUMMY_CREDENTIALS = new String[]
	// {"a@a.a:hello", "bar@example.com:world" };

	/**
	 * The default email to populate the email field with.
	 */
	// public static final String EXTRA_EMAIL =
	// "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;
	Button signUp;
	Button signIn;
	DataHandler dbHandler;
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mUserName;
	private String mPassword;
	private String mFirstName;
	private String mMiddleName;
	private String mLastName;
	private String mTelephone;
	private int atCount;
	private int dotCount;

	// UI references.
	private EditText mEmailView;
	private EditText mUserNameView;
	private EditText mPasswordView;
	private EditText mFirstNameView;
	private EditText mMiddleNameView;
	private EditText mLastNameView;
	private EditText mTelephoneView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private void nextActivity() {
		Intent intent = new Intent(this, CountActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setupActionBar();
		// Set up the login form.
		// mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);
		// signIn = (Button)findViewById(R.id.sign_in_button);
		signUp = (Button) findViewById(R.id.sign_up_button);
		mPasswordView = (EditText) findViewById(R.id.password);
		mUserNameView = (EditText) findViewById(R.id.username);
		mFirstNameView = (EditText) findViewById(R.id.firstname);
		mMiddleNameView = (EditText) findViewById(R.id.middlename);
		mLastNameView = (EditText) findViewById(R.id.lastname);
		mTelephoneView = (EditText) findViewById(R.id.telephone);
		/*
		 * mPasswordView .setOnEditorActionListener(new
		 * TextView.OnEditorActionListener() {
		 * 
		 * @Override public boolean onEditorAction(TextView textView, int id,
		 * KeyEvent keyEvent) { if (id == R.id.login || id ==
		 * EditorInfo.IME_NULL) { attemptLogin(); return true; } return false; }
		 * });
		 */

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

	}

	public void signUp(View view) {
		attemptRegister();
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
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
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

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {
		// Reset errors.

		mEmailView.setError(null);
		mPasswordView.setError(null);
		mUserNameView.setError(null);
		mFirstNameView.setError(null);
		mLastNameView.setError(null);
		mEmail = null;
		mPassword = null;
		mUserName = null;
		mFirstName = null;
		mMiddleName = null;
		mLastName = null;
		mTelephone = null;

		atCount = 0;
		dotCount = 0;
		if (mAuthTask != null) {
			return;
		}
		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		mUserName = mUserNameView.getText().toString();

		mFirstName = mFirstNameView.getText().toString();

		mMiddleName = mMiddleNameView.getText().toString();

		mLastName = mLastNameView.getText().toString();

		mTelephone = mTelephoneView.getText().toString();

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
				mEmailView.setError(getString(R.string.error_noAtTheRate));
				focusView = mEmailView;
				cancel = true;
			} else if (dotCount == 0) { // Added code for . check
				mEmailView.setError(getString(R.string.error_noDot));
				focusView = mEmailView;
				cancel = true;
			} else if (null == mUserName || mUserName.length() == 0) {
				mUserNameView
						.setError(getString(R.string.error_invalid_UserName));
				focusView = mUserNameView;
				cancel = true;
			} else if (null == mFirstName || mFirstName.length() == 0) {
				mFirstNameView
						.setError(getString(R.string.error_invalid_FirstName));
				focusView = mFirstNameView;
				cancel = true;
			} else if (null == mLastName || mLastName.length() == 0) {
				mLastNameView
						.setError(getString(R.string.error_invalid_LastName));
				focusView = mLastNameView;
				cancel = true;
			}
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_up);
			showProgress(true);
			dbHandler = new DataHandler(getBaseContext());
			dbHandler.open();
			GlobalValues globalVar = (GlobalValues) getApplicationContext();
			globalVar.setUserName(mUserName);
			long insertOutput = dbHandler.insertDataUserDetails(mEmail,
					mUserName, mPassword, mFirstName, mMiddleName, mLastName,
					mTelephone);
			globalVar.setLoggedIn(true);
			// Toast.makeText(getBaseContext(), "Data Inserted",
			// Toast.LENGTH_LONG).show();
			dbHandler.close();
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
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
			showProgress(false);

			if (success) {
				// finish(); should go to next page
				nextActivity();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
