package com.RBK;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class RegistrationFunctionExecution {

	public static void ExecuteQRCodeAction(final String qrCode,
			final String name, final String Dob, final String email,
			final Context context) {

		Handler h = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				List<String> postData = new ArrayList<String>();
				postData.add(name);
				postData.add(email);
				postData.add(Dob);
				postData.add(qrCode);

				if (msg.what != 1) {
					// THIS CODE IS EXECUTED WHEN CONNECTION IS NOT PROER
					// Add the qrCode to Sqllite
					UserTableDatabaseHandler userTableDatabaseHandler = new UserTableDatabaseHandler(
							context);
					UserTable user = new UserTable();
					user.setQrCode(qrCode);
					long insertedId = userTableDatabaseHandler.addContact(user,
							context);
					// start Service to send to server ASAP
					Intent intent = new Intent(context, SyncingService.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("name", name);
					intent.putExtra("email", email);
					intent.putExtra("dob", Dob);
					intent.putExtra("qrCode", qrCode);
					context.startService(intent);
					// navigate to the next page with information that the
					// internet is not working
					new NavigationClass().MovetoVisitorCustomerPage(
							(int) insertedId, false, context,
							VisitorCustomer.class, "", qrCode,email,0);
					// TO DO:: ADD ELSE STATEMENT FOR INSERTED ID<0 NAVIGATE TO
					// ERROR PAGE
				} else {
					// THIS CODE IS EXECUTED WHEN CONNECTION IS GOOD
					new registrationAsyncTask(context).execute(postData);
				}
			}
		};

		GloballyUsedFunctions.isNetworkAvailable(h, 5);
		// this time can be SET USING a shared preference thing. 50 IS IN MILLI
		// SECONDS
	}
}

class registrationAsyncTask extends AsyncTask<List<String>, Void, String> {
	private Context context;
	private String name;
	private String qrCode;
	private String dob;
	private String email;

	public registrationAsyncTask(Context c) {
		context = c;
	}

	protected String doInBackground(List<String>... args) {
		List<String> postData = args[0];

		name = postData.get(0);
		email = postData.get(1);
		dob = postData.get(2);
		qrCode = postData.get(3);
		String url = "http://192.168.1.117/BellyReworked1/api/UserApi/InsertNewUser/?Name="
				+ name
				+ "&Email="
				+ email
				+ "&Dob="
				+ dob
				+ "&QRCode="
				+ qrCode;
		return GloballyUsedFunctions.MakeandReceiveHTTPResponse(url);
	}

	protected void onPostExecute(String result) {

		JSONObject jsonObject = null;
		try {
			jsonObject = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String UserInserted = null;
		int UserID = 0;
		try {
			UserInserted = jsonObject.getString("Success");
			UserID = jsonObject.getInt("UserId");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (UserInserted.toLowerCase().contentEquals("false")) {
			new NavigationClass().MovetoVisitorCustomerPage(UserID, context,
					name, qrCode,email,0);
		} else {
			new NavigationClass().MoveToScannerPage(context);
		}

	}

}
