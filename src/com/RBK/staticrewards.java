package com.RBK;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;


import com.google.zxing.client.android.CaptureActivity;

public class staticrewards extends Activity {

	ArrayList<String> mylist = new ArrayList<String>();

	private static final View View = null;
	private ScrollView lv;
	private Context mContext;
	CaptureActivity c = new CaptureActivity();
	Intent intent = getIntent();
	TextView p;
	ImageView t;
	TextView marquee;
	TextView u;
	ImageView i;
	public static String Rewards[];
	public static String RewardsText[];
	int count;
	static int GlobalPoints;
	static staticrewards activityA;
	Boolean setflag;
	String valid;
	View v = null;
	ProgressBar load;
	TextView load1;
	ProgressBar load2;
	TextView load2_text;
	ProgressBar load3;
	TextView load3_text;
	int bp = 0;
	int vp;
	int tp;
	int pp;
	int current_points;
	String col, alt_col;
	String RequiresInternet;
	ArrayAdapter<String> arrayAdapter;
	ArrayList<RewardsInformation> map = new ArrayList<RewardsInformation>();
	AssetManager am;

	Boolean test = false;

	// to be Passed on
	int UserID;
	int CumulativePoints;
	String Invoice;
	String Amount;
	String StoreID;
	boolean writeToSQL;
	int kilosToday;

	// end of to be Passed on

	// *****************Filling in ListView for
	// Rewards*********************************************//

	public List<RewardsInformation> readJSONFeed() {
		RewardsTableDatabaseHandler rewardsdbHandler=new RewardsTableDatabaseHandler(getApplicationContext());
		return rewardsdbHandler.getAllRewards();
	}

	private class JSONFeedTask extends AsyncTask<Void, Void, List<RewardsInformation>> {
		protected List<RewardsInformation> doInBackground(Void... urls) {
			return readJSONFeed();
		}

		protected void onPostExecute(List<RewardsInformation> rewards) {
			try {
				lv = (ScrollView) findViewById(R.id.ListViewForPoints);
				FillListViewTable(rewards);
			} catch (Exception e) {
				Log.d("JSONFeedTask", e.getLocalizedMessage());
			} 

		}
	}

	void FillListViewTable(List<RewardsInformation>rewards) {

		mContext = this.getApplicationContext();

		lv = (ScrollView) findViewById(R.id.ListViewForPoints);
		LinearLayout childln = (LinearLayout) findViewById(R.id.LinearLayoutForPoints);
		childln.setVisibility(View.INVISIBLE);

		int k=0;
		for (int current = 0; current < rewards.size() ; current++) {
			childln.setVisibility(View.INVISIBLE);
			LayoutInflater inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v =  inflater.inflate(R.layout.rewards_text_view, null);
			TextView  rewards_Text1   = (TextView)    v.findViewById(R.id.name_user);
			TextView  rewards_Text   = (TextView)    v.findViewById(R.id.success_message);
			TextView  points_Text   = (TextView)    v.findViewById(R.id.pointsbyk);
			points_Text.setTextSize(0, 20);
			points_Text.setText("" + rewards.get(current).points);

			String imagePath = Environment.getExternalStorageDirectory().toString() + "/sdcard//RBK/downloadedfile"+ k +".jpg";
			rewards_Text.setTextSize(0, 20);
			rewards_Text.setText(rewards.get(current).name);
			rewards_Text1.setOnClickListener(getOnClickDoSomething(rewards_Text,rewards.get(current).name,rewards.get(current).id,(rewards.get(current).points+bp),imagePath));
			k++;
			Log.v("log_tag", "k ::: " + k);
			childln.addView(v);
		}
		load.setVisibility(View.GONE);
		load1.setVisibility(View.GONE);
		childln.setVisibility(View.VISIBLE);
		lv.addView(childln);
	}
	
	public void FillRewardsTable(View view) {
		new JSONFeedTask()
		.execute();
	}

	// *****************Filling in ListView for
	// Rewards*********************************************//

	public String ReadUrlsForJsonResult(String url) {
		return GloballyUsedFunctions.MakeandReceiveHTTPResponse(url);
	}

	// *****************FeedBack for
	// Rewards*********************************************//
	public void fjs(int ans, String qid) {
		new FJSONFeedTask()
				.execute("http://pointsbykilo.azurewebsites.net/api/QuestionAnswerApi/InsertAnswersForQuestion?storeId="
						+ StoreID
						+ "&answerId="
						+ ans
						+ "&questionId="
						+ qid
						+ "&userId=" + UserID);
	}

	private class FJSONFeedTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... urls) {
			return ReadUrlsForJsonResult(urls[0]);
		}

		protected void onPostExecute(String result) {
			try {

				JSONObject jsonObject = new JSONObject(result);

				bp = jsonObject.getInt("BonusPoints");
				tp = jsonObject.getInt("CumulativePoints");
				GlobalPoints = tp;

				t.setVisibility(View.VISIBLE);
				load2.setVisibility(View.GONE);
				load2_text.setVisibility(View.GONE);
				int new_points = current_points + bp;
				p = (TextView) findViewById(R.id.currentpoints);
				p.setTextColor(Color.parseColor("#336600"));
				p.setText("Kilos Earned Today : " + new_points);
				p.setVisibility(View.VISIBLE);
				load3.setVisibility(View.GONE);
				load3_text.setVisibility(View.GONE);
				marquee.setVisibility(View.GONE);
				Feedback.i = false;

			} catch (Exception e) {
				Intent intent = new Intent(getApplicationContext(), Error.class);

				startActivityForResult(intent, 0);
				Log.d("JSONFeedTask", e.getLocalizedMessage());
			}
		}
	}

	// *****************FeedBack for
	// Rewards*********************************************//

	// ********************Motivational Message*************************************************//

	public void Displaymessage() {
		String message[] = { "You're an Inspiration!", "Get a parking lot!",
				"You are a Star!", "Good going Rockstar!", "Up, up and away!",
				"That was Fast!", "Quick! Get a bucket!" };
		// String message[] = {"1","2","3","4","5","6"};
		Random randomGenerator = new Random();
		int randomInt = randomGenerator.nextInt(6);
		Log.v("log_tag", "random int =  " + randomInt);
		TextView a = (TextView) findViewById(R.id.currentmessage);
		a.setTextColor(Color.parseColor(col));
		a.setText(message[randomInt]);

	}

	// *****************End of Motivational
	// Message*********************************************//

	public void RewardsTextClicked(View v) {

	}

	View.OnClickListener getOnClickDoSomething(final TextView textView,
			final String Description, final int id, final int points,
			final String image) {
		return new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), Popup.class);
				intent.putExtra("Description", Description);
				intent.putExtra("rewardID", id);
				intent.putExtra("userID", UserID);
				Log.v("log_tag", "UserID@leavingrewards ::: " + UserID);
				intent.putExtra("points", points);
				intent.putExtra("image", image);
				intent.putExtra("cumulativePoints", CumulativePoints);
				intent.putExtra("pointsToday", kilosToday);
				startActivityForResult(intent, 0);

			};
		};
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_staticrewards);
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		View v = findViewById(R.id.myview3);
		col = prefs.getString("color", "#000000");
		alt_col = prefs.getString("color_alt", "0");
		v.setBackgroundColor(Color.parseColor(col));
		v.setSystemUiVisibility(8);

		StoreID = prefs.getString("storeId", "-999");
		RequiresInternet = prefs.getString("RequiresInternet", "true");
		am = getAssets();
		Log.v("log_tag", "store@REWARDS :: " + StoreID);

		Intent i = getIntent();
		UserID = i.getIntExtra("userId", 0);
		setflag = i.getExtras().getBoolean("Flag");
		Log.v("log_tag", "flag@REwards :::" + setflag);
		Invoice = i.getStringExtra("Invoice");
		Log.v("log_tag", "Invoice@REwards :::" + Invoice);
		Amount = i.getStringExtra("Amount");
		Log.v("log_tag", "Amount@REwards :::" + Amount);
		load = (ProgressBar) findViewById(R.id.load_rewards);
		load.setVisibility(View.VISIBLE);
		load1 = (TextView) findViewById(R.id.load_text_rewards);
		load1.setVisibility(View.VISIBLE);
		load2 = (ProgressBar) findViewById(R.id.load_rewards_tpoints);
		load2_text = (TextView) findViewById(R.id.load_text_rewards_tpoints);
		load3 = (ProgressBar) findViewById(R.id.load_rewards_cpoints);
		load3_text = (TextView) findViewById(R.id.load_text_rewards_cpoints);
		Log.v("log_taag", "on create" + test);
		Button home = (Button) findViewById(R.id.home);
		home.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(v.getContext(), MainActivity.class);
				startActivityForResult(intent, 0);
				finish();

			}

		});

		marquee = (TextView) findViewById(R.id.marquee);
		marquee.setSelected(true);
		marquee.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				Intent intent = new Intent(v.getContext(), Feedback.class);
				startActivityForResult(intent, 0);

			}

		});

		FillRewardsTable(View);
		Displaymessage();
		activityA = this;

		// ///TO DISPALY CURRENT AND CUMULATIVE POINTS/////
		writeToSQL = i.getBooleanExtra("writeToSQL",false);
		kilosToday = i.getIntExtra("kilosToday", -100);
		CumulativePoints = i.getIntExtra("cumulativePoints", 0);

		// HIDING AND SHOWING CURRENT POINTS AND CUMULATIVE POINTS
		p = (TextView) findViewById(R.id.currentpoints);
		p.setTextColor(Color.parseColor(alt_col));
		p.setText("Kilos Earned Today: " + kilosToday);

		load3.setVisibility(View.GONE);
		load3_text.setVisibility(View.GONE);
		t = (ImageView) findViewById(R.id.totalpoints);
		t.setVisibility(View.VISIBLE);
		load2.setVisibility(View.GONE);
		load2_text.setVisibility(View.GONE);
		u = (TextView) findViewById(R.id.displaymessage);
		u.setTextColor(Color.parseColor(alt_col));
		u.setText("browse rewards...");
		// END OF HIDING AND SHOWING CURRENT POINTS AND CUMULATIVE POINTS

	}

	public static staticrewards getInstance() {
		return activityA;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rewards, menu);
		return true;
	}

	@Override
	protected void onResume() {
		System.gc();
		test = Feedback.i;
		Log.v("log_tag ", "on resume:::" + test);
		if (test == true) {
			updatepoints();

		}
		super.onResume();
	}

	private void updatepoints() {
		p.setVisibility(View.INVISIBLE);
		load3.setVisibility(View.VISIBLE);
		load3_text.setText("Updating Your Kilos");
		load3_text.setTranslationX(-35);

		load3_text.setVisibility(View.VISIBLE);

		t.setVisibility(View.INVISIBLE);
		load2.setVisibility(View.VISIBLE);
		load2_text.setTextSize(10);
		Log.v("log", "TextSize@rewards" + load2_text.getTextSize());
		load2_text.setText("Updating Your Kilos");
		load2_text.setTranslationX(-20);

		load2_text.setVisibility(View.VISIBLE);
		int r = Feedback.Rating;

		int ans_id = Feedback.map.get(r - 1).id;
		Log.v("log", "Qid@rewards" + ans_id);
		String Qid = Feedback.QuestionId;
		Log.v("log", "Qid@rewards" + Qid);
		fjs(ans_id, Qid);
		test = false;

	}

	@Override
	protected void onPause() {
		super.onPause();
		System.gc();

		System.gc();

	}

	public void onBackPressed() {

		Intent start = new Intent(staticrewards.this, MainActivity.class);
		startActivity(start);
		finishActivity(0);
	}



	@Override
	protected void onDestroy() {
		super.onDestroy();

		map = null;
		System.gc();
	}

}
