package com.RBK;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginUsingEmail extends Activity {

	String Username;
	EditText id;
	TextView load;
	Button login;
	String name;
	String col;

	ProgressBar load1;
	final Context context = this;
	ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_using_email);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		col = prefs.getString("color", "#000000");
		View v = findViewById(R.id.myview5);
		v.setBackgroundColor(Color.parseColor(col));
		v.setSystemUiVisibility(8);
		load = (TextView) findViewById(R.id.load_text);
		load.setVisibility(View.GONE);
		load1 = (ProgressBar) findViewById(R.id.load);
		load1.setVisibility(View.GONE);
		id = (EditText) findViewById(R.id.email_login1);
		id.setTextColor(Color.parseColor(col));
		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		login = (Button) findViewById(R.id.log_email);
		login.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Username = id.getText().toString();
				Log.v("log_tag", "Username@login ::: " + Username);

				login.setVisibility(View.GONE);
				load.setVisibility(View.VISIBLE);
				load1.setVisibility(View.VISIBLE);
				EmailLoginExecution.ExecuteEmailLogin(Username,
						getApplicationContext(),ctw);

			}

		});

		Button home = (Button) findViewById(R.id.h);
		home.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivityForResult(intent, 0);
				finish();
			}

		});

	}

	public void onBackPressed() {

		Intent start = new Intent(LoginUsingEmail.this, MainActivity.class);
		startActivity(start);
		finishActivity(0);
	}

	@Override
	protected void onResume() {
		System.gc();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		System.gc();
		load.setVisibility(View.GONE);
		load1.setVisibility(View.GONE);

		finish();
		System.gc();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		System.gc();
	}

}
