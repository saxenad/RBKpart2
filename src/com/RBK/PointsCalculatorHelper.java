package com.RBK;

import java.util.List;

import android.content.Context;

public class PointsCalculatorHelper {

	
	public  PointsCalculatorHelper() {
	
	}


	
static public int CalculatePurchasePoints(float amount,Context c)
{
	//retrieve from the database all purchase points
	PurchasePointsDatabaseHandler purchasePointsDbHandler=new PurchasePointsDatabaseHandler(c);
	 List<PurchasePointsPerStoreTable>purchasePoints=purchasePointsDbHandler.getAllPurchasePoints();
	//use the modulus to calculate it
	 int moneyPointsEarned=0;
		for (PurchasePointsPerStoreTable purchasePoint : purchasePoints)
        {
            float moneySpent = purchasePoint.moneySpent;
            int pointsEarned = purchasePoint.pointsEarned;
            int quotient =(int)(amount / moneySpent);
            moneyPointsEarned = moneyPointsEarned + quotient * pointsEarned;
            amount = amount - (moneySpent * quotient);
        }
	    
      return  moneyPointsEarned;
 }
	
static private int  CalculatePointsInSQlite(Context c, String qrCode,String email){
	
	final UserTableDatabaseHandler userTableDatabaseHandler = new UserTableDatabaseHandler(c);
	 List<UserTable>users=null;
	if (email!=null && email!="")users=userTableDatabaseHandler.getContact(qrCode,false);
	
	if(qrCode!=null && qrCode!="") users=userTableDatabaseHandler.getContact(qrCode,email);
	
	int pointsEarned=0;
	 
	 if (users!=null){
	 for (int i = 0; i < users.size(); i++) {
			UserTable userObject = users.get(i);
		 pointsEarned=pointsEarned+userObject.getPointsEarned();
	 }
	 }
	return pointsEarned;
}

static public int CalculateVisitAndCumulativePoints(int cumulativePoints, String qrCode,String email,Context context){

	int pointsInSqlite=CalculatePointsInSQlite(context,qrCode,email);
	int visitPoints=GetTodayVisitPointsOnly(context);
	return cumulativePoints+pointsInSqlite+visitPoints;
}



static public int CalculateInvoiceVisitCumulativePoints(int cumulativePoints, float amount,String qrCode,String email,Context context){

	int pointsInSqlite=CalculatePointsInSQlite(context,qrCode,email);
	int purchasePoints=CalculatePurchasePoints(amount,context);
	int visitPoints=GetTodayVisitPointsOnly(context);
	return cumulativePoints+pointsInSqlite+purchasePoints+visitPoints;
}

static public int GetTodayVisitPointsOnly(Context c){
	return 5;
}


}
