package com.RBK;


public class UserTable {

	int _id;
    String _qrCode;
    //java.util.Date  _insertDate;
    String _insertDate;
    String _invoiceId;
    String _email;
    int _pointsEarned;
    
    String _amount;
    // Empty constructor
    public UserTable(){
    }
    public UserTable(int id, String qrCode,String insertDate){
        this._id = id;
        this._qrCode=qrCode;
        this._insertDate=insertDate;
    }
    
    public UserTable(String qrCode){
        this._qrCode=qrCode;
    }
    
    public UserTable(String email,boolean emailLogin){
        this._email=email;
    }
    
    
 // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
    
    public void setPointsEarned(int i){
        this._pointsEarned = i;
    }
     
    public void setAmount(String amount){
        this._amount =amount;
    }
    
    public void setInvoiceId(String invoiceId){
        this._invoiceId =invoiceId;
    }
    
    public void setEmail(String email){
        this._email =email;
    }
    // getting name
    public String getQrCode(){
        return this._qrCode;
    }
    
    public String getEmail(){
    
    	if (this._email!="") return this._email;
    	else return this._email;
    }
    
    public int getPointsEarned(){
        return this._pointsEarned;
    }
    
    
    public String getInsertDate(){
        return this._insertDate;
    }
    
    public void setInsertDate(String insertDate){
    //    java.util.Date expiredDate = stringToDate(insertDate, "EEE MMM d HH:mm:ss zz yyyy");        

    	this._insertDate=insertDate;
    }
     
    // setting name
    public void setQrCode(String qrCode){
        this._qrCode = qrCode;
    }
    
 	
	
}
