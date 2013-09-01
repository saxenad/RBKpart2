package com.RBK;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.R.string;
import android.content.res.AssetManager;
import android.os.Handler;
import android.util.Log;

public class GloballyUsedFunctions {

public static String MakeandReceiveHTTPResponse(String Url){
		
		return MakeandReceiveHTTPResponse(Url,false,null);
	}
	
	public static String MakeandReceiveHTTPResponse(String Url,boolean override,AssetManager am)
	{
		if (!override){
		StringBuilder stringBuilder = new StringBuilder();
		String Result = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(Url);
		try {
			HttpResponse response = httpClient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				
				Result=ReadHTTPResponse(response);
			} else {
				Log.d("JSON", "Failed to download file");
			}
		} catch (Exception e) {
			Log.d("readJSONFeed", e.getLocalizedMessage());
		}
		return Result;
	}
		else{
			
			return  loadJSONFromFile(am,Url);
		}

	}
	
	public static  String ReadHTTPResponse(HttpResponse response) throws IllegalStateException, IOException
	{
		StringBuilder stringBuilder = new StringBuilder();
		HttpEntity entity = response.getEntity();
		InputStream inputStream = entity.getContent();
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		String line;
		while ((line = reader.readLine()) != null) {
			stringBuilder.append(line);
	}
		inputStream.close();
		return stringBuilder.toString();
}

	public static String loadJSONFromFile(AssetManager am,String url) {
	    String json = null;
        //parse which file to load from url
        
        String[] parsedUrl=url.split("http://pointsbykilo.azurewebsites.net/api/");
        try {
        	
        	String fileNameFromUrl=parsedUrl[1].split("//?")[1];
        	String filePath=fileNameFromUrl;
        	 InputStream someStream=am.open(filePath+".txt");
        	 int size = someStream.available();
            BufferedReader reader = new BufferedReader(new InputStreamReader(someStream));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
              sb.append(line).append("\n");
            }
            return sb.toString();


        } catch (IOException ex) {
           
        	ex.printStackTrace();
            return null;
        }
        

	}

	public static void isNetworkAvailable(final Handler handler,
			final int timeout) {

		// ask fo message '0' (not connected) or '1' (connected) on 'handler'
		// the answer must be send before before within the 'timeout' (in
		// milliseconds)

		new Thread() {

			private boolean responded = false;

			@Override
			public void run() {
				// set 'responded' to TRUE if is able to connect with google
				// mobile (responds fast)
				new Thread() {
					@Override
					public void run() {
						HttpGet requestForTest = new HttpGet(
								"http://m.google.com");
						try {
							new DefaultHttpClient().execute(requestForTest); // can
																				// last...
							responded = true;
						} catch (Exception e) {
						}
					}

				}.start();

				try {
					int waited = 0;
					while (!responded && (waited < timeout)) {
						sleep(100);
						if (!responded) {
							waited += 100;
						}
					}
				} catch (InterruptedException e) {
				} // do nothing
				finally {
					if (!responded) {
						handler.sendEmptyMessage(0);
					} else {
						handler.sendEmptyMessage(1);
					}
				}

			}

		}.start();

	}

	public static Boolean SendUserObjectAsPost(List<UserTable> contacts,String storeID)
			throws ClientProtocolException, IOException, JSONException {

		int counter = 0;
		JSONArray jsonArray = new JSONArray();
	
		StringEntity entity = null;
		JSONObject obj = new JSONObject();
		for (int i = 0; i < contacts.size(); i++) {

			counter = counter + 1;
			String name = "user" + counter;
			UserTable userObject = contacts.get(i);
			obj.put("userId", userObject.getID());
			obj.put("qrCode", userObject.getQrCode());
			obj.put("pointsEarned",userObject.getPointsEarned());
			obj.put("storeId",storeID);
			obj.put("email",userObject.getEmail());
			// obj.put("insertDate", userObject.getInsertDate());
			jsonArray.put(obj);

		}

		String myString = jsonArray.toString();

		HttpPost httpPost = new HttpPost(
				"http://pointsbykilo.azurewebsites.net/api/UserApi/InsertUserSqlLite");
		StringEntity httpPostEntity = new StringEntity(myString, HTTP.UTF_8);
		httpPostEntity.setContentType("application/json");
		httpPost.setEntity(httpPostEntity);
		HttpClient client = new DefaultHttpClient();
		try {
			HttpResponse response = client.execute(httpPost);
			String parsedResponse = ParseJsonResponseFromPost(response);
			return true;
		} catch (Exception e) {

			return false;

		}

	}

	public static String ParseJsonResponseFromPost(HttpResponse response)
			throws IllegalStateException, IOException {
		String Result = null;
		StatusLine statusLine = response.getStatusLine();
		int statusCode = statusLine.getStatusCode();
		if (statusCode == 200) {

			Result = ReadHTTPResponse(response);
		} else {
			Log.d("JSON", "Failed to download file");
		}

		return Result;
	}













}