package com.RBK;

public class PurchasePointsPerStoreTable {


int id;
float moneySpent;
int pointsEarned;
int storeId;

// Empty constructor
public PurchasePointsPerStoreTable(){
}
public PurchasePointsPerStoreTable(int id, int storeId,float moneySpent,int pointsEarned){
    this.id = id;
    this.moneySpent=moneySpent;
    this.pointsEarned=pointsEarned;
    this.storeId=storeId;
}

// getting ID
public int getID(){
    return this.id;
}
 
// setting id
public void setID(int id){
    this.id = id;
}
 
// getting name
public float getmoneySpent(){
    return this.moneySpent;
}

public float getPoints(){
    return this.pointsEarned;
}

//setting id
public void setmoneySpent(int moneySpent){
 this.moneySpent = moneySpent;
}
//setting id
public void setpointsEarned(int pointsEarned){
 this.pointsEarned = pointsEarned;
}

public int getstoreId(){
    return this.storeId;
}
//setting id
public void setstoreId(int storeId){
 this.storeId = storeId;
}

}

