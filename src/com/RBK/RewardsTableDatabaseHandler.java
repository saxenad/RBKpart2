package com.RBK;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class RewardsTableDatabaseHandler  extends SQLiteOpenHelper {

	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION =2;
 
    // Database Name
    private static final String DATABASE_NAME = "RBKAndroidDB1";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "RewardsTable";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_RewardsText = "RewardsText";

    private static final String KEY_RewardsPoints="RewardsPoints";
    
    private static final String KEY_RewardsId="RewardsId";
    
    
	public RewardsTableDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}

	   @Override
	    public void onCreate(SQLiteDatabase db) {
	      //  String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
	        //        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
	        
	    	 String CREATE_RewardsTable = "CREATE TABLE " + TABLE_CONTACTS + "("
	    		               + KEY_ID + " INTEGER PRIMARY KEY," + KEY_RewardsText + " TEXT,"+KEY_RewardsPoints+" REAL,"+KEY_RewardsId+" REAL)";
	    		        
	    	db.execSQL(CREATE_RewardsTable);
		
	        
	}
	   
	   @Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
			  // Drop older table if existed
	        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
	        // Create tables again
	        onCreate(db);
		}
	   
	   public  long addContact(RewardsInformation reward) {
		   
	        SQLiteDatabase db = this.getWritableDatabase();
		   ContentValues values = new ContentValues();
	        values.put(KEY_RewardsText, reward.name);
	        // Contact Name
	        values.put(KEY_RewardsPoints,reward.points);
	        values.put(KEY_RewardsId, reward.id);
	        // Inserting Row
	        long insertedId=db.insert(TABLE_CONTACTS, null, values);
	      //  db.close(); // Closing database connection
	      //  copyDataBase(c);
	        return insertedId;
	   }
		
	   public void RemoveExistingRewards(){
		   
		 try{
			 SQLiteDatabase db = this.getWritableDatabase();
			    String deleteSQL = "DELETE FROM " + TABLE_CONTACTS;
			    db.execSQL(deleteSQL);
		 }
		 
		 catch(Exception e)
		 {
			Log.i("sa","fail");
		 }
		 }
		 
	   
		public void copyDataBase(Context context) {
	        Log.i("info", "in copy data base at finally");
	        try {
	        	
	            File sd = Environment.getExternalStorageDirectory();
	            File data = Environment.getDataDirectory();
	            if (sd.canWrite()) {
	            	
	            	File currentDB =context.getDatabasePath(DATABASE_NAME);

	    	        Log.i("info", "Can Write");

	    	     //  File currentDB=new File(Path);

	              
	                String backupDBPath = TABLE_CONTACTS;
	               
	                File backupDB = new File(sd, backupDBPath);
	                if (currentDB.exists()) {
	                	  Log.i("info", "Writing into DB");
	                    FileChannel src = new FileInputStream(currentDB)
	                            .getChannel();
	                    FileChannel dst = new FileOutputStream(backupDB)
	                            .getChannel();
	                    dst.transferFrom(src, 0, src.size());
	                    src.close();
	                    dst.close();
	                }
	            }
	        } catch (Exception e) {
	            Log.i("info", "in copy of bata base 10 ");

	        }
	    } 
	   
		   public List<RewardsInformation> getAllRewards(boolean requiresInternet,AssetManager am) throws JSONException{
			   
				  List<RewardsInformation> rewardsList = new ArrayList<RewardsInformation>();
			   if (requiresInternet)
			   {
				   String url="http://pointsbykilo.azurewebsites.net/api/RewardsApi/GetStoreRewardsInformation/?storeId=1";
				   String result=GloballyUsedFunctions.loadJSONFromFile(am, url);
				   JSONArray ja = new JSONArray(result);
					for (int i = 0; i < ja.length(); i++) {
						JSONObject e = ja.getJSONObject(i);
						RewardsInformation aa = new RewardsInformation();

						aa.url = e.getString("ImageUrl");
						aa.name = e.getString("RewardsText");
						aa.points = e.getInt("RewardsPoints");
						aa.id = e.getInt("RewardsId");
						rewardsList.add(aa);
			   }
			   }
			   	return rewardsList;
		   }
		   
	   
		public  List<RewardsInformation>getAllRewards() {
	   
			  List<RewardsInformation> rewardsList = new ArrayList<RewardsInformation>();
			    // Select All Query
			//    String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS+"Where qrCode=?",new String[]{qrCode};
			 
			    SQLiteDatabase db = this.getWritableDatabase();
			//    Cursor cursor = db.rawQuery(selectQuery, null);
			    Cursor cursor =db.rawQuery("SELECT * FROM RewardsTable",null);

			    // looping through all rows and adding to list
			    if (cursor.moveToFirst()) {
			        do {
			        	RewardsInformation reward = new RewardsInformation();
			        	reward.setID(Integer.parseInt(cursor.getString(3)));
			        	reward.setpoints(Integer.parseInt(cursor.getString(2)));
			        	reward.setname(cursor.getString(1));
			            // Adding contact to list
			        	rewardsList.add(reward);
			        } while (cursor.moveToNext());
			    }
			 
			    // return contact list
			    return rewardsList;
			
		}
	   }


