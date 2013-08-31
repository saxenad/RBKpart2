package com.RBK;

import android.app.IntentService;
import android.content.Intent;
import android.os.SystemClock;

public class UserInfoIntentService extends IntentService {
    public static final String PARAM_IN_MSG = "imsg";
    public static final String PARAM_OUT_MSG = "omsg";
    public UserInfoIntentService() {
        super("UserInfoIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent) {
    	//here we will get this intent with extra params which need to be sent to the server
    	
    	
        String msg = intent.getStringExtra(PARAM_IN_MSG);
        SystemClock.sleep(30000); // 30 seconds
        
    }
}