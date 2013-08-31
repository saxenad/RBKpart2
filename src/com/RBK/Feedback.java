package com.RBK;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

public class Feedback extends Activity {
	static Boolean i = false;
	static String QuestionId;
	String QuestionText;
	static int Rating;
	String StoreID;
	TextView a;
	Button b;
	TextView z;
	AssetManager am;
	String RequiresInternet;
	ProgressBar load;
	TextView load1;
	RatingBar r;
	String col;

	static ArrayList<AnswersInformation> map = new ArrayList<AnswersInformation>();

	public String readJSONFeed(String URL) {

	return  GloballyUsedFunctions.MakeandReceiveHTTPResponse(URL, true,am);

	}

	private class AJSONFeedTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {

				JSONObject jsonObject = new JSONObject(result);
				JSONObject Questions = jsonObject.getJSONObject("Question");
				QuestionText = Questions.getString("QuestionText");
				a = (TextView) findViewById(R.id.qtext);
				a.setText(QuestionText);
				z.setVisibility(View.VISIBLE);
				a.setVisibility(View.VISIBLE);
				r.setVisibility(View.VISIBLE);
				b.setVisibility(View.VISIBLE);
				load.setVisibility(View.GONE);

				load1.setVisibility(View.GONE);
				QuestionId = Questions.getString("QuestionId");
				Log.v("tag", "::  " + QuestionId);
				JSONArray Answers = jsonObject.getJSONArray("Answers");
				for (int i = 0; i < Answers.length(); i++) {
					JSONObject answer = Answers.getJSONObject(i);
					AnswersInformation aa = new AnswersInformation();
					aa.id = answer.getInt("Id");
					aa.name = answer.getString("Text");
					Log.v("log_tag", "" + aa.id);
					Log.v("log_tag", aa.name);
					map.add(aa);

				}

			} catch (Exception e) {
				Intent intent = new Intent(getApplicationContext(), Error.class);

				startActivityForResult(intent, 0);

				Log.d("JSONFeedTask", e.getLocalizedMessage());
			}
		}
	}

	public void ajs() {
		new AJSONFeedTask()
				.execute("http://pointsbykilo.azurewebsites.net/api/QuestionAnswerApi/GetQuestionForStore/?storeId="
						+ StoreID);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);
		View v = findViewById(R.id.feedback);
		v.setSystemUiVisibility(8);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		StoreID = prefs.getString("storeId", "-999");
		RequiresInternet = prefs.getString("RequiresInternet", "false");
		col = prefs.getString("color", "#000000");
		Log.v("log_tag", "store@FEEDBACK :: " + StoreID);
		// a.setVisibility(View.INVISIBLE);

		load = (ProgressBar) findViewById(R.id.load_feed);

		load1 = (TextView) findViewById(R.id.load_feed_text);

		r = (RatingBar) findViewById(R.id.ratingBar1);
		r.setVisibility(View.INVISIBLE);
		z = (TextView) findViewById(R.id.z);
		z.setVisibility(View.INVISIBLE);
		z.setText("Your FeedBack is Valuabe. \n Please Leave a Rating.");
		am = getAssets();
		ajs();

		r.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {

			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {

				if (rating == 0) {
					b.setEnabled(false);
					z.setText("Please Leave Us a Rating.");

				} else if (rating == 1) {

					b.setEnabled(true);
					z.setText(map.get(0).name);
				} else if (rating == 2) {
					b.setEnabled(true);
					z.setText(map.get(1).name);
				} else if (rating == 3) {
					b.setEnabled(true);
					z.setText(map.get(2).name);
				} else if (rating == 4) {
					b.setEnabled(true);
					z.setText(map.get(3).name);
				} else if (rating == 5) {
					b.setEnabled(true);
					z.setText(map.get(4).name);
				}
			}
		});

		b = (Button) findViewById(R.id.dismiss);
		b.setVisibility(View.INVISIBLE);
		b.setEnabled(false);
		b.setBackgroundColor(Color.parseColor(col));
		b.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Rating = (int) r.getRating();
				if (Rating >= 1) {
					i = true;
				} else {

					i = false;
				}

				finish();

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.feedback, menu);
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
