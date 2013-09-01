package com.RBK;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class SyncingRedeemAwards extends IntentService {
	public static final String PARAM_IN_MSG = "imsg";
	public static final String NOTIFICATION = "com.Goldtrial1.SyncingReceiver";

	public SyncingRedeemAwards() {
		super("SyncingRedeemAwards");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		String storeId= prefs.getString("storeId", "-999");
		int rewardsId = intent.getIntExtra("RewardsId",-999);
		int userId=intent.getIntExtra("userId", -999);
		int cumulativePoints=intent.getIntExtra("cumulativePoints",0);
		String invoiceId=intent.getStringExtra("Invoice");
		String amount=intent.getStringExtra("Amount");
		Boolean RedeemReward=intent.getBooleanExtra("RedeemReward",false);
		int pointsEarnedToday=intent.getIntExtra("pointsToday",0);
		int answerId=intent.getIntExtra("answerId",0);

		Boolean insertResult = false;

		do {
			insertResult = ExecuteSyncingProcess(rewardsId,pointsEarnedToday ,userId, storeId,amount,cumulativePoints,invoiceId,RedeemReward,answerId);
		} while (insertResult == false);

	}

	private boolean ExecuteSyncingProcess(int rewardsId,int pointsEarnedToday,int userId,String storeId,String amount,int cumulativePoints,String invoiceId,Boolean RedeemReward,int answerId) {
		
		String Url=null;
		if (amount==null)
		{
			if (RedeemReward) Url="http://pointsbykilo.azurewebsites.net/api/NewPointsApi/MarkRewardAsRedeemed/?rewardsId="+rewardsId + "&userId="+userId+"&storeId=" + storeId+"&pointsEarnedToday="+pointsEarnedToday+"&answerId="+answerId;
			else Url="http://pointsbykilo.azurewebsites.net/api/NewPointsApi/InsertVisitPoints/?userId="+userId+"&storeId=" + storeId+"&pointsEarnedToday="+pointsEarnedToday+"&answerId="+answerId;
		}
		else
		{
			if (RedeemReward) Url="http://pointsbykilo.azurewebsites.net/api/NewPointsApi/MarkRewardAsRedeemed/?rewardsId="+rewardsId + "&userId="+userId+"&storeId=" + storeId+"&pointsEarnedToday="+pointsEarnedToday+"invoiceId="+invoiceId+"&amount="+amount+"&answerId="+answerId;
			else Url="http://pointsbykilo.azurewebsites.net/api/NewPointsApi/InsertInvoicePoints/?userId="+userId+"&storeId=" + storeId+"&pointsEarnedToday="+pointsEarnedToday+"&invoiceId="+invoiceId+"&amount="+amount+"&answerId="+answerId;
		}

		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Url);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				return true;
			} else {
				return false;
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			Log.i("FailedTOInsert", "failed");
			return false;
		} catch (ClientProtocolException e) {
			Log.i("FailedTOInsert", "failed");
			return false;
		} catch (IOException e) {
			Log.i("FailedTOInsert", "failed");
			return false;
		}
	}


}
