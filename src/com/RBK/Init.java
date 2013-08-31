package com.RBK;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

public class Init extends Activity {
	
	String col;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_init);
		
	Integrate();
		
		}
	
	
	 protected void onResume()
	  {
	    
	    super.onResume();
	    Integrate();
	  }

	
	public void Integrate(){

		PreferenceManager.getDefaultSharedPreferences(this); // this line reads the Preference Manager
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		col = prefs.getString("color", "#000000");
		
		if (col=="null" | col == "#000000")
		{
			Intent start = new Intent(Init.this,LoginActivity.class);
	        startActivity(start);
	        finishActivity(0);
		}
		else 
			{
			 Intent start = new Intent(Init.this,MainActivity.class);
    	        startActivity(start);
    	        finishActivity(0);
	}
	
	
	
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.init, menu);
		return true;
	}

}
