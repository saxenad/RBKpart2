package com.RBK;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

public class SyncingReceiver extends BroadcastReceiver {
	   public static final String ACTION_RESP =
	      "com.Goldtrial1.SyncingReceiver";
	   @Override
	    public void onReceive(Context context, Intent intent) {
	   
		   Log.i("ServiceReceiver","Received");
	    }
	}
