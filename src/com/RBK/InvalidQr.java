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

public class InvalidQr extends Activity {

	String col;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invalid_qr);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		col = prefs.getString("color", "#000000");
		View v = findViewById(R.id.invalidqr);
		col = prefs.getString("color", "#000000");
		 v.setBackgroundColor(Color.parseColor(col));
		v.setSystemUiVisibility(8);
		Button i = (Button) findViewById(R.id.qr_invalid);
		i.setOnClickListener(new OnClickListener (){
			public void onClick(View v){
			
				 Intent start = new Intent(InvalidQr.this, MainActivity.class);
				
				
		startActivity(start);
		finish();
			}
			  
		});
				
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.invalid_qr, menu);
		return true;
	}

}
