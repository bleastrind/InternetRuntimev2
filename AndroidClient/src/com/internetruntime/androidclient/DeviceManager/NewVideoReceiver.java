package com.internetruntime.androidclient.DeviceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NewVideoReceiver extends BroadcastReceiver{

	private DeviceManager handler = null;
	
	public NewVideoReceiver(DeviceManager handler) {
		this.handler = handler;
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		Log.d("tag", "new video: " + intent.getDataString());
		handler.NewVideoHandler(intent);
	}

}
