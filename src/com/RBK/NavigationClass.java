package com.RBK;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NavigationClass {

	void MovetoVisitorCustomerPage(int userId, Context c, String name,
			String qrCode,String email, int cumulativePoints) {
		MovetoVisitorCustomerPage(userId, true, c, VisitorCustomer.class, name,
				qrCode,email,cumulativePoints);
	}

	void MovetoVisitorCustomerPage(int userId, Boolean writeToSQL,
			Context comingFrom, Class GoingTo, String name, String qrCode,String email,int cumulativePoints) {
//		Intent intent = new Intent(comingFrom, SyncingService.class);
	//	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		
		Intent start = new Intent(comingFrom, GoingTo);
		start.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		start.putExtra("userId", userId);
		start.putExtra("name", name);
		start.putExtra("writeToSQL", writeToSQL);
		start.putExtra("qrCode", qrCode);
		start.putExtra("cumulativePoints",cumulativePoints);
		start.putExtra("email",email);
		comingFrom.startActivity(start);

	}

	void MovetoRegistrationPage(Context comingFrom, String qrCode) {

		Intent i = new Intent(comingFrom, Register.class);
		String str = qrCode;
		i.putExtra("QrCode", str);
		comingFrom.startActivity(i);
		// overridePendingTransition(R.anim.slide_in_left,
		// R.anim.slide_out_left);
	}

	void MoveToInvoicePage(Context comingFrom, Class GoingTo, int userId,
			Boolean writeToSQL, Boolean flag,int CumulativePoints, int VisitPointsToday) {
		Intent start = new Intent(comingFrom, GoingTo);
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		start.putExtra("Flag", flag);
		start.putExtra("userId", userId);
		start.putExtra("writeToSQL", writeToSQL);
		start.putExtra("cumulativePoints",CumulativePoints);
		start.putExtra("visitPointsToday", VisitPointsToday);
		comingFrom.startActivity(start);

	}

	void MoveToRewardsPageFromVisitorCustomer(Context comingFrom,
			Class GoingTo, int userId, Boolean writeToSQL, Boolean flag,
			int CumulativePoints, int PointsToday) {

		Intent start = new Intent(comingFrom, GoingTo);
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		start.putExtra("Flag", flag);
		start.putExtra("userId", userId);
		start.putExtra("writeToSQL", writeToSQL);
		start.putExtra("kilosToday", PointsToday);
		start.putExtra("cumulativePoints",CumulativePoints);

		comingFrom.startActivity(start);

	}

	void MoveToRewardsPageFromInvoice(Context comingFrom,
			Class GoingTo, int userId, Boolean writeToSQL, Boolean flag,
			int CumulativePoints, int PointsToday,String Invoice,String Amount)
	{
		
		Intent start = new Intent(comingFrom, Rewards.class);
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		start.putExtra("Flag", flag);
		start.putExtra("userId", userId);
		start.putExtra("writeToSQL", writeToSQL);
		start.putExtra("kilosToday", PointsToday);
		start.putExtra("cumulativePoints",CumulativePoints);
		start.putExtra("Invoice", Invoice);
		start.putExtra("Amount", Amount);
		comingFrom.startActivity(start);
		
	}
	
	void MoveToScannerPage(Context c) {
		Intent start = new Intent(c, Scanner.class);
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startActivity(start);
	}
	
	void MoveToHomePage(Context c) {
		Intent start = new Intent(c, MainActivity.class);
		start.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startActivity(start);
	}
	

	
}
