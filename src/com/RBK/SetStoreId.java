package com.RBK;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetStoreId extends Activity {
String StoreID;
String ChosenInternetRequired;
String Bgcolor;
String alttextcolor;
EditText a;
EditText requiresInternet;
EditText color;
EditText altcolor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_store_id);
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		 a = (EditText) findViewById(R.id.store_id_dy);
		 requiresInternet = (EditText) findViewById(R.id.RequiresInternet);
         color = (EditText) findViewById(R.id.color_app);
         altcolor = (EditText) findViewById(R.id.alt_col);
         this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
         final Button b = (Button) findViewById(R.id.set);
		b.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v)
			{
			StoreID =	a.getText().toString();
			ChosenInternetRequired =requiresInternet.getText().toString();
			Bgcolor = color.getText().toString();
		    alttextcolor = altcolor.getText().toString();    
		        SharedPreferences.Editor editor = prefs.edit();
		        
		        
		        editor.putString("storeId", StoreID);
		        
		        if (ChosenInternetRequired.equalsIgnoreCase("false"))
		        {
			        editor.putString("RequiresInternet","false");

		        }
		        else  editor.putString("RequiresInternet","true");
		        
		        editor.putString("color", Bgcolor) ;
		        editor.putString("color_alt", alttextcolor);	
		       
		        Log.v("log_tag" , "     " + StoreID);// value to store
		        editor.commit();
		        Toast.makeText(getApplicationContext(), "Store Id set to :: " + " " + StoreID  , Toast.LENGTH_LONG).show();
		        b.setVisibility(View.GONE);
		    	Intent intent = new Intent(v.getContext() , Downloader.class);
				startActivityForResult(intent, 0);
				finish();
			}
		});
     
        
        Button h = (Button) findViewById(R.id.home_setstore);
		h.setOnClickListener(new OnClickListener(){
			
			public void onClick(View v)
			{
				Intent intent = new Intent(v.getContext() , MainActivity.class);
				startActivityForResult(intent, 0);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_store_id, menu);
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
		    System.gc();
	    System.gc();
	 
	  }
	 
	  @Override
	  protected void onDestroy()
	  {
	    super.onDestroy();

     
    
	    System.gc();
	  }
	
}
