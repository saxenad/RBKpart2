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
import android.util.Log;

public class SyncingService extends IntentService {
	public static final String PARAM_IN_MSG = "imsg";
	public static final String PARAM_OUT_MSG = "omsg";
	public static final String NOTIFICATION = "com.Goldtrial1.SyncingReceiver";

	public SyncingService() {
		super("SyncingService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		String msg = intent.getStringExtra(PARAM_IN_MSG);
		String name = intent.getStringExtra("name");
		String email = intent.getStringExtra("email");
		String dob = intent.getStringExtra("dob");
		String qrCode = intent.getStringExtra("qrCode");

		Boolean insertResult = false;

		do {
			insertResult = ExecuteSyncingProcess(msg, name, email, dob, qrCode);
		} while (insertResult == false);

	}

	private boolean ExecuteSyncingProcess(String msg, String name,
			String email, String dob, String qrCode) {
		String Url = "http://192.168.1.117/BellyReworked1/api/UserApi/InsertNewUser/?Name="
				+ name
				+ "&Email="
				+ email
				+ "&Dob="
				+ dob
				+ "&QRCode="
				+ qrCode;
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

	// keep trying till u are done..

}
