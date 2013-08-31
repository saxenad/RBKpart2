package com.RBK;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class VisitorCustomer extends Activity {
	int u;
	String col;
	String n;
	String qrCode;
	int UserId;
	boolean writeToSQL;
	Boolean flag;
	int cumulativePoints;
	String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vistorcustomer);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		View v = findViewById(R.id.x);
		col = prefs.getString("color", "#000000");
		v.setBackgroundColor(Color.parseColor(col));
		v.setSystemUiVisibility(8);

		Intent i = getIntent();

		n = i.getStringExtra("name");
		UserId = i.getIntExtra("userId", 0);
		qrCode = i.getStringExtra("qrCode");
		email=i.getStringExtra("email");
		cumulativePoints = i.getIntExtra("cumulativePoints", 0);
		writeToSQL = i.getBooleanExtra("writeToSQL", false);

		Log.v("log_tag", "USERID @ VC :: " + u);
		TextView name = (TextView) findViewById(R.id.name_user);
		name.setText(n);

		Button no = (Button) findViewById(R.id.no);
		no.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				flag = false;
				int totalPointsEarned = PointsCalculatorHelper
						.CalculateVisitAndCumulativePoints(cumulativePoints,
								qrCode,email,getApplicationContext());
				int pointsEarnedToday = PointsCalculatorHelper
						.GetTodayVisitPointsOnly(getApplicationContext());
				MovetoRewardsPage(UserId, totalPointsEarned, pointsEarnedToday);

			}

		});

		Button yes = (Button) findViewById(R.id.yes);
		yes.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				int pointsEarnedToday = PointsCalculatorHelper
						.GetTodayVisitPointsOnly(getApplicationContext());
				MovetoInvoicePage(UserId, cumulativePoints, pointsEarnedToday);

			}

		});

	}

	void MovetoRewardsPage(int userId, int totalPointsEarned,
			int pointsEarnedToday) {

		if (writeToSQL) {
			
			email=getIntent().getStringExtra("email");
			new SyncingDatabases().SendContactsToAPI(getApplicationContext(),
					Rewards.class, qrCode,email,flag, userId, writeToSQL,
					totalPointsEarned, pointsEarnedToday);
		} else {
			// update the SQLITE DATABASE
			UserTableDatabaseHandler userTableDatabaseHandler = new UserTableDatabaseHandler(
					getApplicationContext());
			
			userTableDatabaseHandler.copyDataBase(getApplicationContext());
			userTableDatabaseHandler.updateContact(Float.valueOf(0), "",
					pointsEarnedToday, UserId);
			// END of Updating SQLITE DATABASE
			new NavigationClass().MoveToRewardsPageFromVisitorCustomer(getApplicationContext(), staticrewards.class,UserId, writeToSQL,flag,cumulativePoints,pointsEarnedToday);
		
		}
	}

	void MovetoInvoicePage(int userId, int totalPointsEarned,
			int pointsEarnedToday) {

		if (writeToSQL) {
			new SyncingDatabases().SendContactsToAPI(getApplicationContext(),
					Invoice.class, qrCode,email,flag, userId, writeToSQL,
					totalPointsEarned, pointsEarnedToday);
		} else {
			
			new NavigationClass().MoveToInvoicePage(getApplicationContext(),Invoice.class, UserId, writeToSQL, flag,cumulativePoints,pointsEarnedToday);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.vistor_customer, menu);
		return true;
	}

	@Override
	protected void onResume() {
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
