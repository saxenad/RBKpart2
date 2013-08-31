package com.RBK;

class RewardsInformation{
    String name;
    String url;
    int points;
    int id;
    RewardsInformation(String name, int number,String n, int num)
    {
        this.name = name;
        this.points = number;
        this.id = num;
        this.url = n;
    }
public RewardsInformation() {
	// TODO Auto-generated constructor stub
} 

public void setID(int id){
    this.id = id;
}
public void setpoints(int points){
    this.points = points;
}
public void setname(String name){
    this.name = name;
}


}