package com.RBK;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;   


public class PurchasePointsDatabaseHandler extends SQLiteOpenHelper  {
	
	// All Static variables
    // Database Version
    private static final int DATABASE_VERSION =4;
 
    // Database Name
    private static final String DATABASE_NAME = "RBKAndroidPurchasePoints";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "PurchasePointsTable";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PurchasePointsId = "PurchasePointsId";

    private static final String KEY_StoreId="StoreId";
    
    private static final String KEY_moneySpent="moneySpent";
    
    private static final String KEY_pointsEarned="pointsEarned";
   
    public PurchasePointsDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
      //  String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
        //        + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
    		               + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PurchasePointsId + " REAL,"+KEY_moneySpent+" REAL,"+KEY_pointsEarned+" REAL,"+KEY_StoreId+" REAL)";
    		        
    	db.execSQL(CREATE_CONTACTS_TABLE);
}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		  // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        // Create tables again
        onCreate(db);
	}
	
	   public  long addPurchasePointsPerStore(PurchasePointsPerStoreTable contact) {
	        SQLiteDatabase db = this.getWritableDatabase();
	        ContentValues values = new ContentValues();
	        values.put(KEY_PurchasePointsId, contact.id);
	        // Contact Name
	        values.put(KEY_moneySpent,contact.moneySpent);
	        values.put(KEY_pointsEarned,contact.pointsEarned);
	        values.put(KEY_StoreId,contact.storeId);
	        // Inserting Row
	     long insertedId=db.insert(TABLE_CONTACTS, null, values);
	      //  db.close(); // Closing database connection
	      //  copyDataBase(c);
	        return insertedId;
	        
	}
	
		public  List<PurchasePointsPerStoreTable>getAllPurchasePoints() {
			   
			  List<PurchasePointsPerStoreTable> purchasePoints = new ArrayList<PurchasePointsPerStoreTable>();
			    SQLiteDatabase db = this.getWritableDatabase();
			    Cursor cursor =db.rawQuery("SELECT * FROM PurchasePointsTable ",null);

			    // looping through all rows and adding to list
			    if (cursor.moveToFirst()) {
			        do {
			        	PurchasePointsPerStoreTable purchasePoint = new PurchasePointsPerStoreTable();
			        	purchasePoint.setID(Integer.parseInt(cursor.getString(1)));
			        	purchasePoint.setmoneySpent(Integer.parseInt(cursor.getString(2)));
			        	purchasePoint.setpointsEarned(Integer.parseInt(cursor.getString(3)));
			        	purchasePoint.setstoreId(Integer.parseInt(cursor.getString(4)));
			            // Adding contact to list
			        	purchasePoints.add(purchasePoint);
			        } while (cursor.moveToNext());
			    }
			 
			    // return contact list
			    return purchasePoints;
			
		}
		 public void RemoveExistingPurchasePoints(){
			 
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
	
}
