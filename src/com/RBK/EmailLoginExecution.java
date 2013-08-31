package com.RBK;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextThemeWrapper;

public class EmailLoginExecution {

	
	public static void ExecuteEmailLogin(final String email,final Context context,final ContextThemeWrapper ctw) {

		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		String  RequiresInternet=prefs.getString("RequiresInternet","true");
		
		AssetManager am =context.getAssets();
		if (RequiresInternet.equals("true"))
		{
		Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what != 1) { 
					//THIS CODE IS EXECUTED WHEN CONNECTION IS NOT PROER
					UserTableDatabaseHandler userTableDatabaseHandler = new UserTableDatabaseHandler(context);
					UserTable user=new UserTable();
					user.setEmail(email);
					long insertedId=userTableDatabaseHandler.addContact(user,context);
					if (insertedId>0)
					new NavigationClass().MovetoVisitorCustomerPage((int)insertedId,false,context,VisitorCustomer.class,"","",email,0);
					//TO DO:: ADD ELSE STATEMENT FOR INSERTED ID<0 NAVIGATE TO ERROR PAGE
				} else { 
					//THIS CODE IS EXECUTED WHEN CONNECTION IS GOOD
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
					String StoreID= prefs.getString("storeId", "-999");
					new VerifyUserUsingEmail(context,ctw)
					.execute("http://192.168.1.117/BellyReworked1/api/UserApi/VerifyUserUsingEmail/?email="+email+"&StoreId="+StoreID);
				}
			}
		};
		GloballyUsedFunctions.isNetworkAvailable(h, 5000);
		//this time can be SET USING a shared preference thing. 20 IS IN MILLI SECONDS
	}
	
	}
	
}

class VerifyUserUsingEmail extends AsyncTask<String, Void, String> {
	private Context context;
	private ContextThemeWrapper ctwrapper;
	public VerifyUserUsingEmail (Context c,ContextThemeWrapper ctw){
		context = c;
		ctwrapper=ctw;
	}

	protected String doInBackground(String... urls) {
		return readJSONFeed(urls[0]);
	}

	protected void onPostExecute(String result) {
		try {

			JSONObject jsonObject = new JSONObject(result);
			String vl = jsonObject.getString("Valid");
			String Uid = jsonObject.getString("UserID");
			String name = jsonObject.getString("FullName");
			String qrCode = jsonObject.getString("QrCode");
			String email=jsonObject.getString("Email");
			String cumulativePoints=jsonObject.getString("CumulativePoints");
			
			if (vl.toLowerCase().contentEquals("true")) {

				new NavigationClass().MovetoVisitorCustomerPage(Integer.parseInt(Uid),context, name,qrCode,email,Integer.parseInt(cumulativePoints));

			} else if (vl.toLowerCase().contentEquals("false")) {
			
				
				
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ctwrapper);
            	builder.setTitle("Error")
            	    .setCancelable(false)
            	    .setMessage("Incorrect E-mail")
            	    .setPositiveButton("Home", new DialogInterface.OnClickListener() {
            	    	public void onClick(DialogInterface dialog, int id) {
                  	   new NavigationClass().MoveToHomePage(context);
            	    	}

            	    	});
		
            	AlertDialog dialog = builder.create();
            	dialog.show();
			}
			
		}
            	 
		catch (Exception e) {
			Log.d("JSONFeedTask", e.getLocalizedMessage());
		}
	}

	public String readJSONFeed(String URL) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

		String  RequiresInternet=prefs.getString("RequiresInternet","true");
		
		  
		AssetManager am =context.getAssets();
		if (RequiresInternet.equals("true"))
		{
			return GloballyUsedFunctions.MakeandReceiveHTTPResponse(URL);
		}
		
		else return GloballyUsedFunctions.MakeandReceiveHTTPResponse(URL,true,am); 
	}

}
