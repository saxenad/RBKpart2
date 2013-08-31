package com.RBK;

import java.io.IOException;
import java.util.List;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

public class SyncingDatabases  {
	
	 List<UserTable>Users=null;
	 Context GlobalContext=null;
	 int UserId;
	 int CumulativePoints=0;
	 int PointsToday=0;
	 Boolean WriteToSQL;
	 String Email;
	 Boolean Flag;
	 Class GlobalGoingTo=null;
	 String QrCode;
	 String StoreID;
	public  void SendContactsToAPI(final Context context,Class goingTo,String qrCode,String email,final Boolean flag, final int userId,final Boolean writeToSQL,int cumulativePoints,int pointsToday) 
	{
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		
		 StoreID = prefs.getString("storeId", "-999");

		final UserTableDatabaseHandler userTableDatabaseHandler = new UserTableDatabaseHandler(context);
		GlobalContext=context;
		GlobalGoingTo=goingTo;
		UserId=userId;
		Flag=flag;
		WriteToSQL=writeToSQL;
		Users=userTableDatabaseHandler.getContact(qrCode,email);
		CumulativePoints=cumulativePoints;
		PointsToday=pointsToday;
		QrCode=qrCode;
		Email=email;
		if (Users.size()>0)
			try{
				ajs();
			}
		
		catch(Exception e){
			Log.v("Error in synching",e.getMessage());
		}
		else
		{
			if (goingTo.getName().contains("Rewards")){
				 new NavigationClass().MoveToRewardsPageFromVisitorCustomer(context,goingTo, userId, writeToSQL, flag,CumulativePoints,PointsToday);
			}

			else new NavigationClass().MoveToInvoicePage(context,goingTo, userId, writeToSQL, flag,CumulativePoints,PointsToday);

		}
		
	}
		
		public void ajs(){
			new JSONFeedTask()
			.execute();
		}
			private class JSONFeedTask extends AsyncTask<Void, Void, Boolean> {
			protected Boolean doInBackground(Void... args) {
				try {
					
					return GloballyUsedFunctions.SendUserObjectAsPost(Users,StoreID);
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
		}

			   protected void onPostExecute(Boolean result) {
			      
				   if (result)
					{
						//delete all the records we just inserted into the database
						final UserTableDatabaseHandler userTableDatabaseHandler = new UserTableDatabaseHandler(GlobalContext);
						userTableDatabaseHandler.deleteContact(Users);
						userTableDatabaseHandler.copyDataBase(GlobalContext);
						if (GlobalGoingTo.getName().contains("Rewards"))
						{
							new NavigationClass().MoveToRewardsPageFromVisitorCustomer(GlobalContext, GlobalGoingTo, UserId, WriteToSQL, Flag, CumulativePoints,PointsToday);
						}
						else
						{
							new NavigationClass().MoveToInvoicePage(GlobalContext,GlobalGoingTo, UserId,WriteToSQL, Flag,CumulativePoints,PointsToday);
						}
					}
		}


		}
		
	}
	
