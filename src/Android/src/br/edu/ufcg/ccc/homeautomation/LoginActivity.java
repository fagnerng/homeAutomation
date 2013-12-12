package br.edu.ufcg.ccc.homeautomation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	 */
	// Values for email and password at the time of the login attempt.
	// UI references.
	private EditText mUsernameView;
	private EditText mPasswordView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		startService(new Intent(LoginActivity.this, br.edu.ufcg.ccc.homeautomation.service.NotificationService.class));

		// Set up the login form.
		mUsernameView = (EditText) findViewById(R.id.username);
		mPasswordView = (EditText) findViewById(R.id.password);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if( mUsernameView.getText().equals("")){
							mUsernameView.setText("fagnerng");
							mPasswordView.setText("fagnerng");
						}
						attemptLogin(view);
					}
				});
				
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		return false;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin(View v) {

		mUsernameView.setError(null);
		mPasswordView.setError(null);

		String mEmail = mUsernameView.getText().toString();
		String mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;
		Drawable errorIcon = getResources().getDrawable(R.drawable.ic_launcher);
		 errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));

		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required),errorIcon);
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password),errorIcon);
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mUsernameView.setError(getString(R.string.error_field_required),errorIcon);
			focusView = mUsernameView;
			cancel = true;
		}
		if (cancel) {
			focusView.requestFocus();
		} else {
			RESTManager.getInstance().requestLogin(v.getContext());
		}
	}

}
