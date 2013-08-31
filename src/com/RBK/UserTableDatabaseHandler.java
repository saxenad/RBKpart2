package com.RBK;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

public class UserTableDatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION =13;

	// Database Name
	private static final String DATABASE_NAME = "RBKAndroidDB";

	// Contacts table name
	private static final String TABLE_CONTACTS = "userTables";

	// Contacts Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_QrCode = "qrCode";

	private static final String KEY_InsertDate = "insertDate";

	private static final String KEY_Amount = "amount";

	private static final String KEY_InvoiceId = "invoiceId";

	private static final String KEY_PointsEarned = "pointsEarned";

	private static final String KEY_Email = "email";

	public UserTableDatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
		// + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT)";

		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_QrCode + " TEXT,"
				+ KEY_Amount + " REAL," + KEY_InvoiceId + " TEXT,"
				+ KEY_PointsEarned + " REAL," + KEY_InsertDate + " TEXT,"
				+ KEY_Email + " TEXT)";

		db.execSQL(CREATE_CONTACTS_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
		// Create tables again
		onCreate(db);
	}

	public long addContact(UserTable contact, Context c) {
		SQLiteDatabase db = this.getWritableDatabase();
		java.util.Calendar cal = java.util.Calendar.getInstance();
		java.util.Date utilDate = cal.getTime();

		ContentValues values = new ContentValues();
		values.put(KEY_QrCode, contact.getQrCode());
		// Contact Name
		values.put(KEY_Amount, 0);
		values.put(KEY_InvoiceId, 0);
		values.put(KEY_InsertDate, utilDate.toString());
		values.put(KEY_PointsEarned, 0);
		values.put(KEY_Email, contact.getEmail());
		// Inserting Row
		long insertedId = db.insert(TABLE_CONTACTS, null, values);
		// db.close(); // Closing database connection
		// copyDataBase(c);
		return insertedId;

	}

	public long deleteContact(List<UserTable> contacts) {
		SQLiteDatabase db = this.getWritableDatabase();

		for (UserTable contact : contacts) {
			db.delete(TABLE_CONTACTS, KEY_ID + "=" + contact._id, null);
		}

		return 1;

	}

	// Getting single contact
	public List<UserTable> getContact(String qrCode, String email) {

		List<UserTable> qrCodeUsers = getContact(qrCode, false);
		List<UserTable> emailUsers = getContact(email, true);

		List<UserTable> finalList = new ArrayList<UserTable>();
		for (UserTable contact : qrCodeUsers) {

			finalList.add(contact);
		}

		for (UserTable contact : emailUsers) {

			finalList.add(contact);
		}

		return finalList;
	}

	

	
	public List<UserTable> getContact(String queryString, Boolean checkEmail) {
		List<UserTable> contactList = new ArrayList<UserTable>();

		try {
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor;
			// Cursor cursor = db.rawQuery(selectQuery, null);
			if (!checkEmail)
				cursor = db.rawQuery(
						"SELECT * FROM userTables WHERE qrCode=?;",
						new String[] { queryString });
			else
				cursor = db.rawQuery("SELECT * FROM userTables WHERE email=?;",
						new String[] { queryString });
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					UserTable contact = new UserTable();
					contact.setID(Integer.parseInt(cursor.getString(0)));
					contact.setQrCode(cursor.getString(1));
					contact.setAmount(cursor.getString(2));
					contact.setInvoiceId(cursor.getString(3));
					contact.setPointsEarned(Integer.parseInt(cursor
							.getString(4)));
					contact.setInsertDate(cursor.getString(5));
					contact.setEmail(cursor.getString(6));
					// Adding contact to list
					contactList.add(contact);
				} while (cursor.moveToNext());
			}

			// return contact list

			return contactList;
		} catch (Exception e) {

			Log.i("ERRROR", e.getMessage());
			return null;
		}
	}

	public int updateContact(float amount, String invoice, int pointsEarned,
			int userId) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_ID, userId);
		values.put(KEY_Amount, amount);
		values.put(KEY_InvoiceId, invoice);
		values.put(KEY_PointsEarned, pointsEarned);

		// updating row
		int id = db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(userId) });

		return id;
	}

	public void copyDataBase(Context context) {
		Log.i("info", "in copy data base at finally");
		try {

			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();
			if (sd.canWrite()) {

				File currentDB = context.getDatabasePath(DATABASE_NAME);

				Log.i("info", "Can Write");

				// File currentDB=new File(Path);

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
