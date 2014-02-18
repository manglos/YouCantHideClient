package umbrella.magic.youcanthide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class RegistrationConfirm extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get the intent and extract the message delivered:
		Intent intent = getIntent();
		/*String firstName = intent.getStringExtra(Registration.EXTRA_FIRST_NAME);
		String lastName = intent.getStringExtra(Registration.EXTRA_LAST_NAME);
		String userName = intent.getStringExtra(Registration.EXTRA_USER_NAME);
		String password = intent.getStringExtra(Registration.EXTRA_PASSWORD);
		String passwordConfirm = intent.getStringExtra(Registration.EXTRA_PASSWORD_CONFIRM);*/
		String phoneNumber = intent.getStringExtra(Registration.EXTRA_PHONE_NUMBER);

		// Set the text view as the activity layout:
		setContentView(R.layout.activity_registration_confirm);
		
		// Create the text view:
		TextView textView = (TextView) findViewById(R.id.reg_confirm_text);
		textView.setTextSize(24);
		/*textView.setText("First Name: " + firstName + "\n");
		textView.append("Last Name: " + lastName + "\n");
		textView.append("User Name: " + userName + "\n");
		textView.append("Password: " + password + "\n");
		textView.append("Password Confirm: " + passwordConfirm + "\n");*/
		textView.append("Phone Number: " + phoneNumber + "\n");
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
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* Called when the user clicks the button */
    public void goToNewGame(View view) {
    	System.out.println("Before the issue?");
    	Intent intent = new Intent(this, GameList.class);
    	startActivity(intent);
    }
}
