package com.RBK;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Error extends Activity {

	String col;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		col = prefs.getString("color", "#000000");
		View v = findViewById(R.id.home_error);
		col = prefs.getString("color", "#000000");
		 v.setBackgroundColor(Color.parseColor(col));
		v.setSystemUiVisibility(8);
		Button i = (Button) findViewById(R.id.logo_but);
		i.setOnClickListener(new OnClickListener (){
			public void onClick(View v){
			
			Intent intent = new Intent(v.getContext() , MainActivity.class);
			
			startActivityForResult(intent, 0);
			}
			  
		});
				
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.error, menu);
		return true;
	}

	
}
