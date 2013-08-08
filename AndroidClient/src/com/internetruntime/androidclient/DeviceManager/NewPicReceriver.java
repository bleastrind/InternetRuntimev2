package com.internetruntime.androidclient.DeviceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NewPicReceriver extends BroadcastReceiver{
	
	private DeviceManager handler = null;
	
	
	
	public NewPicReceriver(DeviceManager handler) {
		// TODO Auto-generated constructor stub
		this.handler = handler;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("tag", "new pic: " + intent.getDataString());
		handler.NewPicHandler(intent);
	}

}
