package com.RBK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.zxing.client.android.CaptureActivity;


public class Scanner extends CaptureActivity {
	
static int c =0;
ProgressBar load;
TextView load_text;
String col;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.capturehalf);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		  
		
		View v = findViewById(R.id.myview1);
		col = prefs.getString("color", "#000000");
		 v.setBackgroundColor(Color.parseColor(col));
		v.setSystemUiVisibility(8);
		
		
		load = (ProgressBar) findViewById(R.id.load_scanner);
	    //load.setVisibility(View.GONE);
	    load_text=(TextView) findViewById(R.id.load_text_scanner);
	   // load_text.setVisibility(View.GONE);
	    
	    
	
	
		Button logo =(Button) findViewById(R.id.home_scanner);
		logo.setOnClickListener(new OnClickListener (){
			
			public void onClick(View v){
			
			
			   
			       
			        Intent intent = new Intent(v.getContext() , MainActivity.class);
					
					startActivityForResult(intent, 0);
			    }
			
			  
		});
		Button login =(Button) findViewById(R.id.hu);
		login.setOnClickListener(new OnClickListener (){
			
			public void onClick(View v){
			
				
			       
			        Intent intent = new Intent(v.getContext() , LoginUsingEmail.class);
					
					startActivityForResult(intent, 0);
			    }
			  
		});
		
	
	}
	

	
	@Override
	  protected void onResume()
	  {
	    System.gc();
	    super.onResume();
	  }
	 public void onBackPressed() {

	     Intent start = new Intent(Scanner.this,MainActivity.class);
	        startActivity(start);
	        finishActivity(0);
	        }

	  @Override
	  protected void onPause()
	  {
		  finish();
	    super.onPause();
	    System.gc();
	  }
	  protected void onDestroy()
	  {
	    super.onDestroy();


	    System.gc();
	  }
}
	
		
