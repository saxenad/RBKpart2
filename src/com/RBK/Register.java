package com.RBK;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

public class Register extends Activity {

	String QrCode;
	EditText emailText;
	EditText FirstName;
	EditText LastName;
	EditText Phone;
	DatePicker dob;
	String Fullname, col;
	int sex;
	ProgressBar load;
	TextView load1;
	TextView emailnotvalid;
	String dateOfBirth;
	Button registration;

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();
		if (checked)
			registration.setVisibility(View.VISIBLE);

		// Check which radio button was clicked
		switch (view.getId()) {
		case R.id.radio0:
			if (checked)
				sex = 0;
			break;
		case R.id.radio1:
			if (checked)
				sex = 1;
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Intent intent = getIntent();
		QrCode = intent.getStringExtra("QrCode");
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);
		View v = findViewById(R.id.myview_register);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		v.setSystemUiVisibility(8);
		final Calendar c = Calendar.getInstance();
		c.get(Calendar.MONTH);
		c.get(Calendar.YEAR);
		c.get(Calendar.DAY_OF_MONTH);

		View v123 = findViewById(R.id.v123);
		v123.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(),
						InputMethodManager.RESULT_UNCHANGED_SHOWN);
			}
		});
		col = prefs.getString("color", "#000000");
		v.setBackgroundColor(Color.parseColor(col));
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		emailnotvalid = (TextView) findViewById(R.id.emailnotvalid);
		emailnotvalid.setVisibility(View.GONE);
		registration = (Button) findViewById(R.id.register);
		registration.setVisibility(View.GONE);
		registration.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (FirstName.getText().toString().isEmpty()
						|| LastName.getText().toString().isEmpty()
						|| Phone.getText().toString().isEmpty()) {
					registration.setVisibility(View.GONE);
					emailnotvalid
							.setText("Fields Cannot be Left Blank.Please Check if You've Filled All The Required Fields");
					emailnotvalid.setVisibility(View.VISIBLE);
					FirstName.addTextChangedListener(new LocalTextWatcher());
					LastName.addTextChangedListener(new LocalTextWatcher());
					Phone.addTextChangedListener(new LocalTextWatcher());
				}

				else if (dob.getDayOfMonth() == c.get(Calendar.DAY_OF_MONTH)
						&& dob.getMonth() == c.get(Calendar.MONTH)
						&& dob.getYear() >= c.get(Calendar.YEAR)) {
					registration.setVisibility(View.GONE);
					emailnotvalid
							.setText("Incorrect Date Of Birth. Please Try Again ! ");
					emailnotvalid.setVisibility(View.VISIBLE);

					dob.init(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
							c.get(Calendar.DATE), dateSetListener);

				}

				else if (Phone.getText().toString().length() < 10) {
					registration.setVisibility(View.GONE);
					emailnotvalid
							.setText("Incorrect Phone Number. Please Check if You have Entered all the digits. ");
					emailnotvalid.setVisibility(View.VISIBLE);
					Phone.addTextChangedListener(new LocalTextWatcher());
				} else {

					if (isEmailValid(emailText.getText().toString())) {
						registration.setVisibility(View.GONE);
						load.setVisibility(View.VISIBLE);

						SendJson();

					} else {
						emailText
								.addTextChangedListener(new LocalTextWatcher());
						registration.setVisibility(View.GONE);
						emailnotvalid
								.setText("Please Try Again , Invalid Email Address !");
						emailnotvalid.setVisibility(View.VISIBLE);
					}
				}
			}
		});
		load = (ProgressBar) findViewById(R.id.load_reg);
		load.setVisibility(View.GONE);
		// load1=(TextView) findViewById(R.id.load_pop_text);
		// load1.setVisibility(View.INVISIBLE);

		ImageView home = (ImageView) findViewById(R.id.register_home);
		home.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivityForResult(intent, 0);

			}
		});
		// registration button functions
		InputValidation();
	}

	private void InputValidation() {
		emailText = (EditText) findViewById(R.id.email);
		emailText.setTextColor(Color.parseColor(col));
		FirstName = (EditText) findViewById(R.id.firstname);
		FirstName.setTextColor(Color.parseColor(col));
		LastName = (EditText) findViewById(R.id.Lastname);
		LastName.setTextColor(Color.parseColor(col));
		Phone = (EditText) findViewById(R.id.phone);
		Phone.setTextColor(Color.parseColor(col));
		dob = (DatePicker) findViewById(R.id.datePicker1);

	}

	private DatePicker.OnDateChangedListener dateSetListener = new DatePicker.OnDateChangedListener() {

		@Override
		public void onDateChanged(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			updateSubmitButtonState();
		}
	};

	void SendJson() {

		String firstName = FirstName.getText().toString();
		String userEmail = emailText.getText().toString();

		dateOfBirth = dob.getMonth() + "." + dob.getDayOfMonth() + "."
				+ dob.getYear();

		Log.v("log_tag", dateOfBirth);
		String lastName = LastName.getText().toString();
		String phoneNumber = Phone.getText().toString();
		Fullname = firstName + " " + lastName;

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		String  RequiresInternet=prefs.getString("RequiresInternet","true");
		AssetManager am =getApplicationContext().getAssets();
		if (RequiresInternet.equals("true"))
		{
		RegistrationFunctionExecution.ExecuteQRCodeAction(QrCode, firstName,
				dateOfBirth, userEmail, getApplicationContext());
		}
		
		else{
			new NavigationClass().MovetoVisitorCustomerPage(1,getApplicationContext(), firstName,QrCode,userEmail,0);
		}

	}

	// END OF JSON FUNCTIONS
	public boolean isEmailValid(String email) {
		String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
				+ "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
				+ "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
				+ "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
				+ "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		if (matcher.matches())
			return true;
		else
			return false;
	}

	// NAVIGATION FUNCTIONS
	void MovetoVisitorCustomerPage(int userId, String name) {
		Intent start = new Intent(Register.this, VisitorCustomer.class);

		start.putExtra("userId", userId);
		start.putExtra("name", name);

		startActivity(start);

	}

	void MovetoSplashScreen() {
		Context view = findViewById(android.R.id.content).getRootView()
				.getContext();
		Intent intent = new Intent(view, Scanner.class);
		startActivityForResult(intent, 1);
	}

	// END OF NAVIGATION FUNCTIONS

	// These functions are for validation to make sure the user has entered some
	// value
	void updateSubmitButtonState() {

		registration.setVisibility(View.VISIBLE);
		emailnotvalid.setVisibility(View.GONE);

	}

	private class LocalTextWatcher implements TextWatcher {
		public void afterTextChanged(Editable s) {
			updateSubmitButtonState();

		}

		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// i dont think we need to use any
		}

		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}
	}

	// END OF REGISTRATION
	// FUNCTIONS/////////////////////////////////////////////
	@Override
	protected void onResume() {
        overridePendingTransition( R.anim.animation_enter, R.anim.animation_leave);
		System.gc();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

		finish();

		System.gc();

	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		System.gc();
	}

}
