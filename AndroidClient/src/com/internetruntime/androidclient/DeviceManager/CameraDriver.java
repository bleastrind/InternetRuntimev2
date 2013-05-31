package com.internetruntime.androidclient.DeviceManager;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

public class CameraDriver {
	public static final int CAMERA_REQUEST_CODE = 1;
	private Intent intent;
	private Context context;
	
	public CameraDriver(Context context) {
		this.context = context;
		intent = new Intent("android.media.action.IMAGE_CAPTURE");		
	}
	
	public Uri takePicture()
	{
		File tmpFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
		Uri originalUri = Uri.fromFile(tmpFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, originalUri);
		((Activity)context).startActivityForResult(intent, CAMERA_REQUEST_CODE);
		return originalUri;
	}
	
}
