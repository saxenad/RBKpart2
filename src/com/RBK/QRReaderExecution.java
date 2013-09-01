package com.RBK;

import org.json.JSONObject;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;

public class QRReaderExecution {

	public static void ExecuteQRCodeAction(final String qrCode,final Context context) {

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
					user.setQrCode(qrCode);
					long insertedId=userTableDatabaseHandler.addContact(user,context);
					if (insertedId>0)
					new NavigationClass().MovetoVisitorCustomerPage((int)insertedId,false,context,VisitorCustomer.class,"",qrCode,"",0);
					//TO DO:: ADD ELSE STATEMENT FOR INSERTED ID<0 NAVIGATE TO ERROR PAGE
				} else { 
					//THIS CODE IS EXECUTED WHEN CONNECTION IS GOOD
					SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
					String StoreID= prefs.getString("storeId", "-999");
					new VerifyUserAPI(context)
					.execute("http://pointsbykilo.azurewebsites.net/api/UserApi/VerifyUser/?QRCode="
							+ qrCode+"&StoreID="+StoreID);
				}
			}
		};
		GloballyUsedFunctions.isNetworkAvailable(h, 5000);
		//this time can be SET USING a shared preference thing. 20 IS IN MILLI SECONDS
	}
		else{
			//ONLY FOR DEMO PURPOSES WILL BE REMOVED ASAP
			String firstFour = qrCode.substring(0, Math.min(qrCode.length(),4));
			
			if(firstFour.toLowerCase().contains("test")||qrCode.contentEquals("RBK36472410421")||qrCode.contentEquals("RBK67916210540"))
			{
				new NavigationClass().MovetoRegistrationPage(context,qrCode);
			}
			else new VerifyUserAPI(context)
			.execute("http://pointsbykilo.azurewebsites.net/api/UserApi/VerifyUser/?QRCode="+ qrCode);
		}
	}
}

/***************************************************/

/* Interaction with API */
/***************************************************/
class VerifyUserAPI extends AsyncTask<String, Void, String> {
	private Context context;
	public VerifyUserAPI (Context c){
		context = c;
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
				
				new NavigationClass().MovetoRegistrationPage(context,qrCode);
			}

		} catch (Exception e) {
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
/***************************************************/

