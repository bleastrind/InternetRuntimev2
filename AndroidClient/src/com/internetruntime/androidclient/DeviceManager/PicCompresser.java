package com.internetruntime.androidclient.DeviceManager;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.util.Log;

public class PicCompresser {
	private Uri uri; 
	private final int COMPRESS_RATIO = 40;
	
	public PicCompresser(Uri uri) {
		this.uri = uri;		
	}
	
	public File getCompressedPic(int reqWitdh,  int reqHeight)
	{
		final Options options = new Options();
		
		options.inJustDecodeBounds = true;
		Log.d("comm", "path: " + uri.getPath());
		try
		{
		BitmapFactory.decodeFile(uri.getPath(), options);
		}catch (Exception e)
		{
			Log.d("comm", "EXCEPTION: " + e.toString());
		}
		Log.d("comm", "height: " + options.outHeight);
		
		options.inSampleSize = calculateInSampleSize(options, reqWitdh, reqHeight);
		Log.d("comm", "inSampleSize: " + calculateInSampleSize(options, reqWitdh, reqHeight));
		Log.d("comm", "options: " + options.toString());
		options.inJustDecodeBounds = false;
		Bitmap bitmap = null;
		try
		{
		bitmap = BitmapFactory.decodeFile(uri.getPath(), options); 
		}catch (OutOfMemoryError e)
		{
			Log.d("comm", "EXCEPTION: " + e.toString());
		}
		Log.d("comm", "bitmap: " + bitmap.toString());
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, COMPRESS_RATIO, byteArrayOutputStream);
		Log.d("comm", "get stream");
		File f = new File(uri.getPath());
		try
		{
			FileOutputStream fileOutputStream = new FileOutputStream(f);
			BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(byteArrayOutputStream.toByteArray());
		}catch(Exception e)
		{			
		}
		return f;
	}
	
	public static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) 
		{
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}
}
