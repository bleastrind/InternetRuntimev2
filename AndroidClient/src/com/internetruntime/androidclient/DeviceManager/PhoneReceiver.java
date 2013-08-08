package com.internetruntime.androidclient.DeviceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
public class PhoneReceiver extends BroadcastReceiver{

	private DeviceManager handler = null;
	
	public PhoneReceiver() {
		// TODO Auto-generated constructor stub
	}
	
	public PhoneReceiver(DeviceManager dm) {
		// TODO Auto-generated constructor stub
		this.handler = dm;
	}
	
	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		Log.d("tag", "get new phone intent");
		handler.PhoneManager(arg1);
	}

}
