package com.RBK;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Success extends Activity {
	int p;
	Timer timer = new Timer();
	String s,col,alt_col;
	 MediaPlayer mp;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_success);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		alt_col=prefs.getString("color_alt", "0");
		View v = findViewById(R.id.myview2);
		v.setSystemUiVisibility(8);
		col = prefs.getString("color", "#000000");
		 v.setBackgroundColor(Color.parseColor(col));
		
		Rewards.getInstance().finish();
		
		Intent a=getIntent();
		p=a.getIntExtra("success", 0);
		s=a.getStringExtra("sm");
		
		Log.v("log_tag", "remaining points :" +p );
		Log.v("log_tag", "sm===" + s );
		
		TextView t = (TextView) findViewById(R.id.pp);
		t.setTextColor(Color.parseColor(alt_col));
		t.setText(String.valueOf(p));
		
		TextView u = (TextView) findViewById(R.id.success_message);
		
		u.setText(Html.fromHtml("&ldquo;"+ s + "&rdquo;"));
		
		mp = MediaPlayer.create(getApplicationContext(), R.raw.clap);
		    mp.start();
		    //Timer timer = new Timer();
		timer.schedule(new TimerTask() {

		   public void run() {

			Intent intent = new Intent(getBaseContext() , MainActivity.class);
			startActivityForResult(intent, 0);

		   }

		}, 30000);
	
	
	Button x = (Button) findViewById(R.id.homer);
	
	x.setOnClickListener(new OnClickListener (){
		public void onClick(View v){
		
			timer.cancel();
			timer.purge();
			System.gc();
		
			Intent intent = new Intent(v.getContext() , MainActivity.class);
		
		startActivityForResult(intent, 0);
		finish();
		}
		
		  
	});
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.success, menu);
		return true;
		
	}
	@Override
	  protected void onResume()
	  {
	    System.gc();
	    super.onResume();
	  }

	  @Override
	  protected void onPause()
	  {
	    super.onPause();
	    timer.cancel();
		timer.purge();
		System.gc();
		mp.release();
		
		finish();
	    
	    System.gc();
	 
	  }
	  public void onBackPressed() {

		     Intent start = new Intent(Success.this,MainActivity.class);
		        startActivity(start);
		        finishActivity(0);
		        }
	 
	
protected void onDestroy()
	{
		super.onDestroy();
		timer.cancel();
		timer.purge();
		System.gc();
		
	}

}
