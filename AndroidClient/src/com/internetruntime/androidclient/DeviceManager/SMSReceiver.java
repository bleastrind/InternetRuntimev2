package com.internetruntime.androidclient.DeviceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver{

	private DeviceManager handler = null;
	
	public SMSReceiver() {
		// TODO Auto-generated constructor stub
	}
	
	public SMSReceiver(DeviceManager handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		

		Log.d("tag", "new SMS recerived");
		handler.SMSRcvHandler(intent);
	}
	
}
