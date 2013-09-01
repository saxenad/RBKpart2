package com.RBK;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Downloader extends Activity {

	// button to show progress dialog
	EditText storeid_dynamic;
	Button btnShowProgress;
	Button btnShowProgress1;
	boolean enablefeaturedbtn = true;
	boolean enableimagesbtn = true;
	int j = 0;
	float k = 0;
	int q = 0;
	float r = 0;
	// Progress Dialog
	private ProgressDialog pDialog;

	ImageView my_image;
	// Progress dialog type (0 - for Horizontal progress bar)
	public static final int progress_bar_type = 0;

	// File url to download

	ArrayList<String> mylist = new ArrayList<String>();

	Intent intent = getIntent();
	int UserID;
	String StoreID;
	public static String Rewards[];
	public static String RewardsText[];
	int count;
	String col;
	static int GlobalPoints;
	Drawable RedeemButton;
	Drawable DisableRedeemButton;
	ArrayAdapter<String> arrayAdapter;
	ArrayList<RewardsInformation> map = new ArrayList<RewardsInformation>();
	ArrayList<RewardsInformation> map1 = new ArrayList<RewardsInformation>();
	ArrayList<PurchasePointsPerStoreTable> purchasePointsPerStoreMap = new ArrayList<PurchasePointsPerStoreTable>();

	ContextThemeWrapper ctw = new ContextThemeWrapper(this, R.style.AppTheme);

	public String readJSONFeed(String URL) {

		return GloballyUsedFunctions.MakeandReceiveHTTPResponse(URL);
	}

	private class JSONFeedTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {

				JSONArray ja = new JSONArray(result);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject e = ja.getJSONObject(i);
					RewardsInformation aa = new RewardsInformation();

					aa.url = e.getString("ImageUrl");
					aa.name = e.getString("RewardsText");
					aa.points = e.getInt("RewardsPoints");
					aa.id = e.getInt("RewardsId");
					map.add(aa);
				}

			} catch (Exception e) {

				AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
				builder.setTitle("Error")
						.setMessage(
								"Oops Something Went Wrong , please try again")
						.setNeutralButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent start = new Intent(
												Downloader.this,
												LoginActivity.class);
										startActivity(start);
										finishActivity(0);

									}
								});
				Log.d("JSONFeedTask", e.getLocalizedMessage());
			}
		}
	}

	private class AJSONFeedTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {

				JSONArray ja = new JSONArray(result);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject e = ja.getJSONObject(i);
					RewardsInformation ab = new RewardsInformation();

					ab.url = e.getString("Value");
					map1.add(ab);
				}

			} catch (Exception e) {

				AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
				builder.setTitle("Error")
						.setMessage(
								"Oops Something Went Wrong , please try again")
						.setNeutralButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent start = new Intent(
												Downloader.this,
												LoginActivity.class);
										startActivity(start);
										finishActivity(0);

									}
								});

				AlertDialog dialog = builder.create();
				dialog.show();
				Log.d("JSONFeedTask", e.getLocalizedMessage());
			}
		}
	}

	private class purchasePointsFeedTask extends
			AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return readJSONFeed(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {

				JSONArray ja = new JSONArray(result);
				for (int i = 0; i < ja.length(); i++) {
					JSONObject e = ja.getJSONObject(i);
					PurchasePointsPerStoreTable ab = new PurchasePointsPerStoreTable();

					ab.id = e.getInt("PurchasePointsId");
					ab.storeId = e.getInt("StoreId");
					ab.moneySpent = e.getInt("moneySpent");
					ab.pointsEarned = e.getInt("pointsEarned");

					purchasePointsPerStoreMap.add(ab);
				}

			} catch (Exception e) {

				AlertDialog.Builder builder = new AlertDialog.Builder(ctw);
				builder.setTitle("Error")
						.setMessage(
								"Oops Something Went Wrong , please try again")
						.setNeutralButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										Intent start = new Intent(
												Downloader.this,
												LoginActivity.class);
										startActivity(start);
										finishActivity(0);

									}
								});

				AlertDialog dialog = builder.create();
				dialog.show();
				Log.d("JSONFeedTask", e.getLocalizedMessage());
			}
		}
	}

	public void js() {
		new JSONFeedTask()
				.execute("http://pointsbykilo.azurewebsites.net/api/PointsApi/GetStoreRewardsInformation/?storeId="
						+ StoreID);
	}

	public void ajs() {
		new AJSONFeedTask()
				.execute("http://pointsbykilo.azurewebsites.net/api/PointsApi/GetFeaturedImagesForRewards/?storeId="
						+ StoreID);
	}

	public void purchasePointsPerStore() {
		new purchasePointsFeedTask()
				.execute("http://pointsbykilo.azurewebsites.net/api/InvoiceApi/GetPurchasePointsPerStore/?storeId=1");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_downloader);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		StoreID = prefs.getString("storeId", "-999");
		Log.v("log_tag", StoreID);
		col = prefs.getString("color", "#000000");
		View v = findViewById(R.id.downloader);
		v.setBackgroundColor(Color.parseColor(col));

		js();
		ajs();
		purchasePointsPerStore();

		// show progress bar button
		btnShowProgress = (Button) findViewById(R.id.btnProgressBar);

		btnShowProgress1 = (Button) findViewById(R.id.Button01);

		// Image view to show image after downloading
		// my_image = (ImageView) findViewById(R.id.my_image);
		/**
		 * Show Progress bar click event
		 * */
		btnShowProgress.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				RewardsTableDatabaseHandler rtdb = new RewardsTableDatabaseHandler(
						v.getContext());
				rtdb.RemoveExistingRewards();

				for (int current = 0; current < map.size(); current++) {

					Log.v("log_tag", "Current ::: " + current);
					RewardsInformation aa = map.get(current);
					if (rtdb.addContact(aa) > 0)
						continue;
					new DownloadFileFromURL().execute(map.get(current).url);
				}
				PurchasePointsDatabaseHandler purchasePointsdb = new PurchasePointsDatabaseHandler(
						v.getContext());
				purchasePointsdb.RemoveExistingPurchasePoints();

				for (int current = 0; current < purchasePointsPerStoreMap
						.size(); current++) {
					// we will add stuff to the sql table
					PurchasePointsPerStoreTable aa = purchasePointsPerStoreMap
							.get(current);
					;
					if (purchasePointsdb.addPurchasePointsPerStore(aa) > 0)
						continue;

				}

				// rtdb.copyDataBase(v.getContext());
				// purchasePointsdb.copyDataBase(v.getContext());

			}
		});
		btnShowProgress1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				for (int current = 0; current < map1.size(); current++) {

					Log.v("log_tag", "Current@ajs ::: " + current);
					new DownloadFeaturedFromURL().execute(map1.get(current).url);

				}
			}
		});
		Button home = (Button) findViewById(R.id.hud);
		home.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivityForResult(intent, 0);
				finish();
			}

		});

	}

	/**
	 * Showing Dialog
	 * */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Downloading Images....");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(false);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}

	/**
	 * Background Async Task to download file
	 * */
	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;

			try {

				File folder = new File(
						Environment.getExternalStorageDirectory() + "/sdcard");
				boolean success = true;
				if (!folder.exists()) {
					success = folder.mkdir();
				}
				File folder2 = new File(
						Environment.getExternalStorageDirectory()
								+ "/sdcard/RBK");
				if (!folder2.exists()) {
					success = folder2.mkdir();
				}

				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();

				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream to write file

				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/sdcard/RBK/downloadedfile" + j + ".jpg");

				byte data[] = new byte[1024];

				long total = 0;
				j++;
				Log.v("log_tag", "j ::: " + j);

				while ((count = input.read(data)) != -1) {
					total += count;

					// writing data to file
					output.write(data, 0, count);

				}

				// flushing output
				output.flush();

				// closing streams
				input.close();

			} catch (Exception e) {
				Intent intent = new Intent(getApplicationContext(), Error.class);

				startActivityForResult(intent, 0);
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));

		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/

		@Override
		protected void onPostExecute(String file_url) {

			// Reading image path from sdcard

			String imagePath = Environment.getExternalStorageDirectory()
					.toString() + "/sdcard/RBK/downloadedfile" + k + ".jpg";
			// setting downloaded into image view

			// my_image.setImageDrawable(Drawable.createFromPath(imagePath));
			k++;

			Log.v("log_tag", "k ::: " + k);
			float y = (k / map.size()) * 100;
			publishProgress("" + (int) (y));
			if (k == map.size())

			{
				pDialog.setProgress(0);
				btnShowProgress.setEnabled(false);
				dismissDialog(progress_bar_type);

			}

		}

	}

	class DownloadFeaturedFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Bar Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_bar_type);
		}

		/**
		 * Downloading file in background thread
		 * */
		@Override
		protected String doInBackground(String... f_url) {
			int count;

			try {
				URL url = new URL(f_url[0]);
				URLConnection conection = url.openConnection();
				conection.connect();
				// getting file length
				int lenghtOfFile = conection.getContentLength();

				File folder = new File(
						Environment.getExternalStorageDirectory() + "/sdcard");
				boolean success = true;
				if (!folder.exists()) {
					success = folder.mkdir();
				}
				File folder2 = new File(
						Environment.getExternalStorageDirectory()
								+ "/sdcard/RBK");
				if (!folder2.exists()) {
					success = folder2.mkdir();
				}
				// input stream to read file - with 8k buffer
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				// Output stream to write file
				OutputStream output = new FileOutputStream(Environment
						.getExternalStorageDirectory().toString()
						+ "/sdcard/RBK/FeaturedImage" + q + ".jpg");

				byte data[] = new byte[1024];

				long total = 0;
				q++;
				Log.v("log_tag", "q::: " + q);

				while ((count = input.read(data)) != -1) {
					total += count;

					// writing data to file
					output.write(data, 0, count);

				}

				// flushing output
				output.flush();

				// closing streams
				input.close();

			} catch (Exception e) {
				Intent intent = new Intent(getApplicationContext(), Error.class);

				startActivityForResult(intent, 0);
				Log.e("Error: ", e.getMessage());
			}

			return null;
		}

		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			pDialog.setProgress(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/

		@Override
		protected void onPostExecute(String file_url) {

			// Displaying downloaded image into image view
			// Reading image path from sdcard

			String imagePath = Environment.getExternalStorageDirectory()
					.toString() + "sdcard/RBK/FeaturedImage" + r + ".jpg";
			// setting downloaded into image view

			// my_image.setImageDrawable(Drawable.createFromPath(imagePath));
			r++;

			Log.v("log_tag", "r ::: " + r);
			float x = (r / map1.size()) * 100;
			publishProgress("" + (int) x);
			if (r == map1.size())

			{

				pDialog.setProgress(0);
				btnShowProgress1.setEnabled(false);
				dismissDialog(progress_bar_type);

			}

		}

	}

	public void CreateTextFileFromJson() {

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
		System.gc();

	}

	public void onBackPressed() {

		Intent start = new Intent(Downloader.this, LoginActivity.class);
		startActivity(start);
		finishActivity(0);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		System.gc();
	}

}