package com.RBK;


public class RewardsTable{
	
	int _id;
    String _RewardsText;
    int  _RewardsId;
    int _RewardsPoints;
    
    // Empty constructor
    public RewardsTable(){
    }
    
    public RewardsTable(int id, String rewardsText, int rewardsPoints, int rewardsId ){
        this._id = id;
        this._RewardsId=rewardsId;
        this._RewardsPoints=rewardsPoints;
        this._RewardsText=rewardsText;
    }
    
 
    
    
 // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    
 	
}
