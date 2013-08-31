package com.RBK;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Popup extends Activity {

	String abc;
	String M;
	String u;
	int UserId;
	int Rid;
	Button red;
	int iden;
	TextView y;
	ImageView my_image;
	ProgressBar load;
	TextView load1;
	String col;
	String StoreID;
	Boolean writeToSQL;
	int cumulativePoints;
	int pointsEarnedToday;
	String Invoice;
	Float Amount;
	int rewardID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_popup);

		View v = findViewById(R.id.myview4);

		v.setSystemUiVisibility(8);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		StoreID = prefs.getString("storeId", "-999");
		Log.v("log_tag", "store@POPUP :: " + StoreID);
		col = prefs.getString("color", "#000000");
		Intent i = getIntent();

		abc = i.getStringExtra("Description");
		u = i.getStringExtra("image");
		UserId = i.getIntExtra("userID", 0);
		writeToSQL = i.getBooleanExtra("writeToSQL", false);
		Rid = i.getIntExtra("rewardID", 0);
		cumulativePoints = i.getIntExtra("cumulativePoints", 0);
		pointsEarnedToday = i.getIntExtra("pointsToday", 0);
		Invoice = i.getStringExtra("Invoice");
		Amount = i.getFloatExtra("Amount", -999);
		rewardID = i.getIntExtra("rewardID", -100);

		final TextView x = (TextView) findViewById(R.id.description);
		x.setText(abc);
		my_image = (ImageView) findViewById(R.id.gf);
		my_image.setImageDrawable(Drawable.createFromPath(u));

		load = (ProgressBar) findViewById(R.id.load_pop);
		load.setVisibility(View.GONE);
		load1 = (TextView) findViewById(R.id.load_pop_text);
		load1.setVisibility(View.INVISIBLE);

		red = (Button) findViewById(R.id.redeem);
		red.setBackgroundColor(Color.parseColor(col));
		red.setOnClickListener(new OnClickListener() {

			public void onClick(final View v) {

				my_image.setVisibility(View.INVISIBLE);
				y.setVisibility(View.INVISIBLE);
				x.setVisibility(View.INVISIBLE);
				red.setVisibility(View.INVISIBLE);
				load.setVisibility(View.VISIBLE);
				load1.setVisibility(View.VISIBLE);
				MovetoSuccessPage();
			}

		});

		check();
	}

	public void check() {

		y = (TextView) findViewById(R.id.remaining);

		Intent i = getIntent();
		int check = i.getIntExtra("points", 0);
		int Gb = cumulativePoints;
		int s = Gb - check;

		if (writeToSQL) {
			if (Gb >= check) {

			} else {
				red.setVisibility(View.INVISIBLE);

				y.setText("You Need " + (check - Gb) + " More Points");

			}
		} else {
			red.setVisibility(View.INVISIBLE);
			y.setText("Poor Internet Connection....Rewards are Browse Only...");
		}

	}

	public void MovetoSuccessPage() {
		Intent i = getIntent();
		int check = i.getIntExtra("points", 0);
		int Gb = cumulativePoints;
		int pointsRemaining = Gb - check;

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());

		String RequiresInternet = prefs.getString("RequiresInternet", "true");

		AssetManager am = getApplicationContext().getAssets();
		if (RequiresInternet.equals("true")) {

			Intent intent = new Intent(getApplicationContext(),
					SyncingRedeemAwards.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("userId", UserId);
			intent.putExtra("cumulativePoints", pointsRemaining);
			intent.putExtra("pointsToday", pointsEarnedToday - check);
			intent.putExtra("RedeemReward", true);
			intent.putExtra("Amount", Amount);
			intent.putExtra("Invoice", Invoice);
			intent.putExtra("RewardsId", rewardID);
			getApplicationContext().startService(intent);
		}

		Intent navigationIntent = new Intent(getApplicationContext(),
				Success.class);
		navigationIntent.putExtra("success", pointsRemaining);
		navigationIntent.putExtra("sm", abc);
		startActivityForResult(navigationIntent, 0);

	}

	@Override
	protected void onResume() {
		System.gc();
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		my_image.setImageDrawable(null);
		my_image.setBackgroundDrawable(null);
		my_image.setImageBitmap(null);
		finish();

		System.gc();

	}

	@Override
	protected void onDestroy() {
		my_image.setImageDrawable(null);
		my_image.setBackgroundDrawable(null);
		my_image.setImageBitmap(null);

		super.onDestroy();

		System.gc();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.popup, menu);
		return true;
	}

}
