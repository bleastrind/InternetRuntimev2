package com.internetruntime.androidclient.Communication;

import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.internetruntime.androidclient.MainActivity;
import com.internetruntime.androidclient.DeviceManager.CameraDriver;
import com.internetruntime.androidclient.DeviceManager.PicCompresser;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class SocketHandler {
	private Context context;
	
	
	public SocketHandler(Context context) {
		this.context = context;
	}
	

	public void handle(String data) 
	{
		String type = null;
		String query = null;
		try {
			JSONObject object = new JSONObject(data);
			type = object.getJSONObject("value").getString("type").toString();
			
			query = object.getJSONObject("value").getString("query").toString();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (type.equals("getPic"))
		{
			final String token = query.split("&")[0].split("=")[1];
			final int reqWidth = Integer.parseInt(query.split("&")[1].split("=")[1]);
			final int reqHeight = Integer.parseInt(query.split("&")[2].split("=")[1]);

			Log.d("comm", token);
			Log.d("comm", String.valueOf(reqWidth));
			Log.d("comm", String.valueOf(reqHeight));
			
			((MainActivity)context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					MainActivity.askForPic(token, reqWidth, reqHeight);
				}
			});
		}
		else if (type.equals("getAddress"))
		{
			final String token = query.split("&")[0].split("=")[1];
			((MainActivity)context).runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					MainActivity.askForAddress(token);
				}
			});
		}
			
		
	}
	
	public void handle(byte[] data)
	{
		
	}
}
