
package com.RBK;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity {

	
	

int k=0;
String col ;
static int f=0;
static int e=0;
 ImageView a ;
  
 ImageView b;
ImageView c ;
ImageView part_logo;
ImageView d ;

AlphaAnimation animation1 ;
AlphaAnimation animation2; 
AlphaAnimation animation3 ;
AlphaAnimation animation4 ;
AlphaAnimation animation5 ;
AlphaAnimation animation6 ;
AlphaAnimation animation7 ;
AlphaAnimation animation8 ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	
		
		PreferenceManager.getDefaultSharedPreferences(this); // this line reads the Preference Manager
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		col = prefs.getString("color", "#000000");
		
		View v = findViewById(R.id.myview);
		  v.setBackgroundColor(Color.parseColor(col));
			v.setSystemUiVisibility(8);
			
			part_logo = (ImageView) findViewById(R.id.partlogo);
			String logopath = Environment.getExternalStorageDirectory().toString() + "/sdcard/RBK/logo.png";

			part_logo.setImageDrawable(Drawable.createFromPath(logopath));
			
		ImageView exit = (ImageView) findViewById(R.id.partner_logo);
		exit.setOnClickListener(new OnClickListener (){
		public void onClick(View v){
		
			e++;
		    if (e == 5) {
		        e = 0;
		        Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.addCategory(Intent.CATEGORY_HOME);
		        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		        startActivity(intent);
		    }
		}
		  
	});
	
	Button logo =(Button) findViewById(R.id.logo_but);
	logo.setOnClickListener(new OnClickListener (){
		public void onClick(View v){
		
			f++;
		    if (f == 5) {
		        f = 0;
		        Intent intent = new Intent(v.getContext() , LoginActivity.class);
				
				startActivityForResult(intent, 0);
		    }
		}
		  
	});
	
	
	
	Log.v("log_tag" , "color ::" + col);
	
	  a = (ImageView) findViewById(R.id.imageView2);
	  
	 b = (ImageView) findViewById(R.id.imageView3);
	c = (ImageView) findViewById(R.id.imageView4);

	 d = (ImageView) findViewById(R.id.imageView5);

	 animation1 = new AlphaAnimation(0.3f, 1.0f);
	 animation2 = new AlphaAnimation(1.0f, 0.3f);
	 animation3 = new AlphaAnimation(0.3f, 1.0f);
	 animation4 = new AlphaAnimation(1.0f, 0.3f);
	 animation5 = new AlphaAnimation(0.3f, 1.0f);
	 animation6 = new AlphaAnimation(1.0f, 0.3f);
	 animation7 = new AlphaAnimation(0.3f, 1.0f);
	 animation8 = new AlphaAnimation(1.0f, 0.3f);
	Log.v("log_tag", "k:: @main " + k);
	
	String imagePath = Environment.getExternalStorageDirectory().toString() + "/sdcard/RBK/FeaturedImage"+ k +".jpg";

	a.setImageDrawable(Drawable.createFromPath(imagePath));
	a.setAlpha(0.0f);
	k++;
	Log.v("log_tag", "k:: @main " + k);
	String imagePath2 = Environment.getExternalStorageDirectory().toString() + "/sdcard/RBK/FeaturedImage"+ k +".jpg";
	b.setImageDrawable(Drawable.createFromPath(imagePath2));
	b.setAlpha(0.0f);
	k++;
	Log.v("log_tag", "k:: @main " + k);
	String imagePath3 = Environment.getExternalStorageDirectory().toString() + "/sdcard/RBK/FeaturedImage"+ k +".jpg";
	c.setImageDrawable(Drawable.createFromPath(imagePath3));
	c.setAlpha(0.0f);
	k++;
	Log.v("log_tag", "k:: @main " + k);
String imagePath4 = Environment.getExternalStorageDirectory().toString() + "/sdcard/RBK/FeaturedImage"+ k +".jpg";
	d.setImageDrawable(Drawable.createFromPath(imagePath4));
	d.setAlpha(0.0f);
	
		System.gc();
	
	
	

	
    animation1.setDuration(3000);
    //animation1.setStartOffset(5000);

    //animation1 AnimationListener
    animation1.setAnimationListener(new AnimationListener(){

        @Override
        public void onAnimationEnd(Animation arg0) {
            // start animation2 when animation1 ends (continue)
            a.startAnimation(animation2);
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation arg0) {
            a.setAlpha(1.0f);
            animation1.reset();

        }

    });
    System.gc();
   
    
    animation2.setDuration(3000);
    animation2.setStartOffset(1000);

    //animation2 AnimationListener
    animation2.setAnimationListener(new AnimationListener(){

        @Override
        public void onAnimationEnd(Animation arg0) {
            // start animation1 when animation2 ends (repeat)
            a.clearAnimation();
            a.setAlpha(0.0f);
            b.startAnimation(animation3);
        }

        @Override
        public void onAnimationRepeat(Animation arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onAnimationStart(Animation arg0) {
            // TODO Auto-generated method stub
        	 animation2.reset();

        }

    });

    System.gc();
    
   
	
	animation3.setDuration(3000);
	
	    
	    animation3.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {
	            
	            b.startAnimation(animation4);
	        }

	        @Override
	        public void onAnimationRepeat(Animation arg0) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onAnimationStart(Animation arg0) {
	            // TODO Auto-generated method stub
	        	 b.setAlpha(1.0f);
	        	 animation3.reset();
	        }

	    });
	    System.gc();
	    
	    animation4.setDuration(3000);
	    animation4.setStartOffset(1000);

	    
	    animation4.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {
	           
	            b.clearAnimation();
	            b.setAlpha(0.0f);
	            c.startAnimation(animation5);
	        }

	        @Override
	        public void onAnimationRepeat(Animation arg0) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onAnimationStart(Animation arg0) {
	            // TODO Auto-generated method stub
	        	 animation4.reset();
	        }

	    });
	   
	    System.gc();
	   animation5.setDuration(3000);
	    
	    animation5.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {
	            
	            c.startAnimation(animation6);
	        }

	        @Override
	        public void onAnimationRepeat(Animation arg0) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onAnimationStart(Animation arg0) {
	            c.setAlpha(1.0f);
	            animation5.reset();

	        }

	    });
	    System.gc();
	    
	    animation6.setDuration(3000);
	    animation6.setStartOffset(1000);

	    
	    animation6.setAnimationListener(new AnimationListener(){

	        @Override
	        public void onAnimationEnd(Animation arg0) {
	            
	            c.clearAnimation();
	            c.setAlpha(0.0f);
	            d.startAnimation(animation7);
	        }

	        @Override
	        public void onAnimationRepeat(Animation arg0) {
	            // TODO Auto-generated method stub

	        }

	        @Override
	        public void onAnimationStart(Animation arg0) {
	            // TODO Auto-generated method stub
	        	 animation6.reset();
	        }

	    });

	    
	    System.gc();
	   
	
		
		animation7.setDuration(3000);
		
		    
		    animation7.setAnimationListener(new AnimationListener(){

		        @Override
		        public void onAnimationEnd(Animation arg0) {
		           
		            d.startAnimation(animation8);
		        }

		        @Override
		        public void onAnimationRepeat(Animation arg0) {
		            // TODO Auto-generated method stub

		        }

		        @Override
		        public void onAnimationStart(Animation arg0) {
		            // TODO Auto-generated method stub
		        	 d.setAlpha(1.0f);
		        	 animation7.reset();
		        }

		    });

		    System.gc();
		    animation8.setDuration(3000);
		    animation8.setStartOffset(1000);

		    
		    animation8.setAnimationListener(new AnimationListener(){

		        @Override
		        public void onAnimationEnd(Animation arg0) {
		           
		            d.clearAnimation();
		            d.setAlpha(0.0f);
		            a.startAnimation(animation1);
		        }

		        @Override
		        public void onAnimationRepeat(Animation arg0) {
		            // TODO Auto-generated method stub

		        }

		        @Override
		        public void onAnimationStart(Animation arg0) {
		            // TODO Auto-generated method stub
		        	 animation8.reset();
		        }

		    });
a.startAnimation(animation1);
		    
System.gc();
		    
		    
		
	Button reg = (Button) findViewById(R.id.login);
	reg.setOnClickListener(new OnClickListener (){
		public void onClick(View v){
		
		Intent intent = new Intent(v.getContext() , LoginUsingEmail.class);
		
		startActivityForResult(intent, 0);
		}
		  
	});
	
	

	Button tap = (Button) findViewById(R.id.tap);
	tap.setOnClickListener(new OnClickListener (){
		
		public void onClick(View v){
			
			
			
		Intent intent = new Intent(v.getContext() , Scanner.class);
		startActivityForResult(intent, 0);
		}
		  
	});
	
	


	
}
	 public void onBackPressed() {

	    // Intent start = new Intent(MainActivity.this,MainActivity.class);
	      //  startActivity(start);
	      //  finishActivity(0);
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
	    a.setImageDrawable(null);
	    a.setBackgroundDrawable(null);
	    a.clearAnimation();
	    System.gc();
	    b.setImageDrawable(null);
	    b.setBackgroundDrawable(null);
	    b.clearAnimation();
	    System.gc();
	    c.setImageDrawable(null);
	    c.setBackgroundDrawable(null);
	    c.clearAnimation();
	    System.gc();
	    d.setImageDrawable(null);
	    d.setBackgroundDrawable(null);
	    d.clearAnimation();
	    System.gc();
	  //  animation1=null;
	    System.gc();
	  //  animation2=null;
	    System.gc();
	   // animation3=null;
	    System.gc();
	 //   animation4=null;
	    System.gc();
	  //  animation5=null;
	    System.gc();
	  //  animation6=null;
	    System.gc();
	  //  animation7=null;
	    System.gc();
	  //  animation8=null;
	    System.gc();
	    
	    System.gc();
	    finish();
	    System.gc();
	  }
	  
	
	public void onDestroy() {
	    super.onDestroy();
	   
	    
	    a.setImageDrawable(null);
	    a.setBackgroundDrawable(null);
	    a.clearAnimation();
	    System.gc();
	    b.setImageDrawable(null);
	    b.setBackgroundDrawable(null);
	    b.clearAnimation();
	    System.gc();
	    c.setImageDrawable(null);
	    c.setBackgroundDrawable(null);
	    c.clearAnimation();
	    System.gc();
	    d.setImageDrawable(null);
	    d.setBackgroundDrawable(null);
	    d.clearAnimation();
	    System.gc();
	    animation1=null;
	    System.gc();
	    animation2=null;
	    System.gc();
	    animation3=null;
	    System.gc();
	    animation4=null;
	    System.gc();
	    animation5=null;
	    System.gc();
	    animation6=null;
	    System.gc();
	    animation7=null;
	    System.gc();
	    animation8=null;
	    System.gc();
	    
	    System.gc();
	}
	
	
}
	


