package com.RBK;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Invoice extends Activity {

	String col;
	Button done;
	ProgressBar load;
	TextView load1;
	int u;
	String StoreID;
	ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);
	String Valid;
	String Invoice;
	String Amount;
	Boolean lag;
	boolean writeToSQL;
	int userId;
	int visitPointsToday;
	int cumulativePoints;
	String QrCode;
	String Email;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invoice);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		View v = findViewById(R.id.invoice);
		col = prefs.getString("color", "#000000");
		StoreID = prefs.getString("storeId", "-999");
		v.setBackgroundColor(Color.parseColor(col));
		v.setSystemUiVisibility(8);
		View v123 = findViewById(R.id.v321);
		v123.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(),
						InputMethodManager.RESULT_UNCHANGED_SHOWN);
			}
		});
		load = (ProgressBar) findViewById(R.id.load_invoice);
		load.setVisibility(View.GONE);
		load1 = (TextView) findViewById(R.id.load_invoice_text);
		load1.setVisibility(View.GONE);

		this.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Intent i = getIntent();
		userId = i.getIntExtra("userId", 0);
		writeToSQL = i.getBooleanExtra("writeToSQL", false);
		visitPointsToday = i.getIntExtra("visitPointsToday", 5);
		cumulativePoints = i.getIntExtra("cumulativePoints", 0);
		QrCode = i.getStringExtra("qrCode");
		Email=i.getStringExtra("email");
		final EditText invoice = (EditText) findViewById(R.id.invoice_myview);
		invoice.setTextColor(Color.parseColor(col));
		final EditText amount = (EditText) findViewById(R.id.amount_spent);
		amount.setTextColor(Color.parseColor(col));
		amount.addTextChangedListener(new LocalTextWatcher());

		done = (Button) findViewById(R.id.submit_invoice);
		done.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				done.setVisibility(View.GONE);
				load.setVisibility(View.VISIBLE);
				load1.setVisibility(View.VISIBLE);
				lag = true;
				Invoice = invoice.getText().toString();
				Amount = amount.getText().toString();
				int cumulativePointsEarned = PointsCalculatorHelper
						.CalculateInvoiceVisitCumulativePoints(
								cumulativePoints, Float.valueOf(Amount),
								QrCode,Email,v.getContext());

				int invoicePointsEarned = PointsCalculatorHelper
						.CalculatePurchasePoints(Float.valueOf(Amount),
								v.getContext());
				int pointsEarnedToday = invoicePointsEarned + visitPointsToday;

				if (writeToSQL) {
					MovetoRewardsPage(userId, pointsEarnedToday,
							cumulativePointsEarned);
					// new
					// UserTableDatabaseHandler(v.getContext()).copyDataBase(v
					// .getContext());
				}

				else {
					UserTableDatabaseHandler userTableDatabaseHandler = new UserTableDatabaseHandler(
							v.getContext());
					userTableDatabaseHandler.updateContact(
							Float.valueOf(Amount), Invoice, pointsEarnedToday,
							userId);
					MovetoRewardsPage(userId, pointsEarnedToday,
							cumulativePoints);

				}

			}

		});

		done.setVisibility(View.INVISIBLE);

		Button skip = (Button) findViewById(R.id.skip_invoice);
		skip.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				lag = false;
				MovetoRewardsPage(userId, visitPointsToday, cumulativePoints);
			}

		});

	}

	void updateSubmitButtonState() {
		boolean enabled = true;
		if (enabled)
			done.setVisibility(View.VISIBLE);

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
			// dont think we have to use these
		}
	}

	void MovetoRewardsPage(int userId, int pointsEarnedToday,
			int cumulativePoints) {

		if (writeToSQL) {

			new NavigationClass().MoveToRewardsPageFromInvoice(
					getApplicationContext(), Rewards.class, userId, writeToSQL,
					lag, cumulativePoints, pointsEarnedToday, Invoice, Amount);

		} else {


			Intent start = new Intent(Invoice.this, staticrewards.class);
			start.putExtra("userId", userId);
			start.putExtra("writeToSQL", writeToSQL);
			start.putExtra("kilosToday", pointsEarnedToday);

			startActivity(start);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invoice, menu);
		return true;
	}

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
