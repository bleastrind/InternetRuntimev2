package com.internetruntime.androidclient;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import com.example.internetruntime.androidclient.R;
import com.internetruntime.androidclient.Communication.HttpDriver;
import com.internetruntime.androidclient.Communication.SocketDriver;
import com.internetruntime.androidclient.DeviceManager.CameraDriver;
import com.internetruntime.androidclient.DeviceManager.DeviceManager;
import com.internetruntime.androidclient.DeviceManager.NewPicReceriver;
import com.internetruntime.androidclient.DeviceManager.NewVideoReceiver;
import com.internetruntime.androidclient.DeviceManager.PhoneReceiver;
import com.internetruntime.androidclient.DeviceManager.PicCompresser;
import com.internetruntime.androidclient.DeviceManager.SMSObserver;
import com.internetruntime.androidclient.DeviceManager.SMSReceiver;
import com.internetruntime.androidclient.UI.CheckBoxItem;
import com.internetruntime.androidclient.UI.EditTextItem;
import com.internetruntime.androidclient.UI.LableTextItem;
import com.internetruntime.androidclient.UI.ListMenu;
import com.internetruntime.androidclient.UI.MenuItem;
import com.internetruntime.androidclient.UI.RadioTextItem;
import com.internetruntime.androidclient.UI.UIActivity;
import com.internetruntime.androidclient.UI.UIConsole;
import com.internetruntime.androidclient.UI.UIConst;
import com.internetruntime.androidclient.UI.UILogField;
import com.internetruntime.androidclient.UI.UIMenu;
import com.internetruntime.androidclient.UI.UIMenuButtonItem;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TaskStackBuilder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class MainActivity extends UIActivity {

	public static DeviceManager dm;
	public static SocketDriver socketClient;
	public static EventHandler eventHandler;
	
	public static Context mainContext;
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			if (requestCode == CameraDriver.CAMERA_REQUEST_CODE)
			{
				Log.d("comm", "Activity Result!");
				((MainActivity)mainContext).runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						MainActivity.dm.handlePicture();
					}
				});
				
			}
			
		}
	}
	
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		UIConst.Init(this);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		clock();
		regDeviceReceiversAndOberservers();
		
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WALLPAPER);
		
		MainActivity.mainContext = this;
		
		Log.d("tag", "after clock");
		
		socketClient = new SocketDriver("1", "1", this);
		socketClient.connect();
		
		
		
		ListMenu menu1 = new ListMenu(this);
		
		MenuItem item1 = new MenuItem(this);
		item1.setText("Sign in");
		MenuItem item2 = new MenuItem(this);
		item2.setText("Settings");
		MenuItem item3 = new MenuItem(this);
		item3.setText("About");
		
		
		
		
		
		
		
		menu1.addItem(item1);
		menu1.addItem(item2);
		menu1.addItem(item3);
		
		start(menu1);
		
		LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		getMainLayout().addView(menu1, layoutParams);
		
		ListMenu menu2 = new ListMenu(this);
		MenuItem item21 = new LableTextItem(this);
		item21.setText("UserName");
		MenuItem item22 = new EditTextItem(this);
		MenuItem item23 = new LableTextItem(this);
		item23.setText("PassWord");
		EditTextItem item24 = new EditTextItem(this);
		item24.setPassword();
		MenuItem item25 = new MenuItem(this);
		item25.setText("Sign in");
		
		menu2.addItem(item21);
		menu2.addItem(item22);
		menu2.addItem(item23);
		menu2.addItem(item24);
		menu2.addItem(item25);
		
		getMainLayout().addView(menu2);		
		item1.setSubMenu(menu2);
		
		ListMenu settingMenu = new ListMenu(this);
		
		MenuItem permission = new MenuItem(this);
		permission.setText("Permissions");
		MenuItem launch = new MenuItem(this);
		launch.setText("LaunchMode");
		MenuItem network = new MenuItem(this);
		network.setText("NetworkMode");
		
		settingMenu.addItem(permission);
		settingMenu.addItem(launch);
		settingMenu.addItem(network);
		item2.setSubMenu(settingMenu);
		getMainLayout().addView(settingMenu);		
		
		
		ListMenu permMenu = new ListMenu(this);
		
		CheckBoxItem callCamera = new CheckBoxItem(this);
		callCamera.setText("Camera    ");
		CheckBoxItem callGPS = new CheckBoxItem(this);
		callGPS.setText("GPS");
		CheckBoxItem newPic = new CheckBoxItem(this);
		newPic.setText("Pic");
		CheckBoxItem newVideo = new CheckBoxItem(this);
		newVideo.setText("Video ");
		CheckBoxItem phoneInOutComing = new CheckBoxItem(this);
		phoneInOutComing.setText("Phone  ");
		CheckBoxItem SMSIncoming = new CheckBoxItem(this);
		SMSIncoming.setText("SMSIn  ");
		CheckBoxItem NewOutcoming = new CheckBoxItem(this);
		NewOutcoming.setText("SMSOut     ");
		
		permMenu.addItem(callCamera);
		permMenu.addItem(callGPS);
		permMenu.addItem(newPic);
		permMenu.addItem(newVideo);
		permMenu.addItem(phoneInOutComing);
		permMenu.addItem(SMSIncoming);
		permMenu.addItem(NewOutcoming);
		
		permission.setSubMenu(permMenu);
		getMainLayout().addView(permMenu);	
		
		
		ListMenu launMenu = new ListMenu(this);
		RadioTextItem auto = new RadioTextItem(this);
		auto.setText("Auto");
		RadioTextItem manu = new RadioTextItem(this);
		manu.setText("Manual    ");
		
		launMenu.addItem(auto);
		launMenu.addItem(manu);
		
		launch.setSubMenu(launMenu);
		getMainLayout().addView(launMenu);	
		
		ListMenu netMenu = new ListMenu(this);
		RadioTextItem always = new RadioTextItem(this);
		always.setText("Always   ");
		RadioTextItem need = new RadioTextItem(this);
		need.setText("Necessary       ");
		
		netMenu.addItem(always);
		netMenu.addItem(need);
		
		network.setSubMenu(netMenu);
		getMainLayout().addView(netMenu);
		
		
		
//		TextView hello = (TextView) findViewById(R.id.label);
//		ViewPropertyAnimator.animate(hello).setDuration(3000).rotationYBy(720).x(100).y(100);
		
		/*
		final UIMenu menu = new UIMenu(this);
		
		for (int i = 0; i < 5; i++)
		{
			final UIMenuButtonItem button = new UIMenuButtonItem(this);
			button.setWidth(UIConst.BTN_WIDTH);
			button.setHeight(UIConst.BTN_HEIGHT);
			button.setText("Test " + i);
			button.setClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("ui", "clicked!");				
					menu.showSubMenu(menu.indexOf(button));
					
				}
			});
			menu.addItem(button);
		}
		Init(menu);
		
		UIConsole console = new UIConsole(this);
		
		console.setWidth(UIConst.CONSOLE_WIDTH);
		console.setHeight(UIConst.CONSOLE_HEIGHT);
		console.setBackground(UIConst.CONSOLE_BACKGROUNG);
		console.render(getMainLayout());
		
		final UIMenu AnotherMenu = new UIMenu(this);
		for (int i = 0; i < 3; i++)
		{
			final UIMenuButtonItem button = new UIMenuButtonItem(this);
			button.setWidth(UIConst.BTN_WIDTH);
			button.setHeight(UIConst.BTN_HEIGHT);
			button.setText("More" + i);
			button.setClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.d("ui", "clicked!");
//					AnotherMenu.hide(AnotherMenu.indexOf(button));
				}
			});
			AnotherMenu.addItem(button);
		}
		menu.get(0).setRelativeMenu(AnotherMenu);
		
//		UILogField logField = new UILogField(this);
//		logField.render();
		*/
	}
	
	
	public static void askForAddress(final String token)
	{
		Log.d("dev", "ask innn");	
		Builder builder = new Builder(MainActivity.mainContext);
		builder.setMessage("A address request from TestApp, Accept?");
		builder.setTitle("Address request");
		builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				MainActivity.dm.takeAddress(token);
			}
		});
		builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
			}
		});
		Log.d("dev", "before show");
		builder.create().show();
		Log.d("dev", "after show");
		
		((MainActivity)mainContext).showNotification("Ask for an address");
	} 
	
	
	public static void askForPic(final String token, final int reqWidth, final int reqHeight)
	{
		Log.d("dev", "ask innn");	
		Builder builder = new Builder(MainActivity.mainContext);
		builder.setMessage("A picture request from TestApp, Accept?");
		builder.setTitle("Picture request");
		builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				MainActivity.dm.takePicture(token, reqWidth, reqHeight);
			}
		});
		builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//				dialog.dismiss();
			}
		});
		Log.d("dev", "before show");
		builder.create().show();
		Log.d("dev", "after show");
		
		((MainActivity)mainContext).showNotification("Ask for a pictrue");
		
		
	}
	
	private void showNotification(String word)
	{
		Intent intent = new Intent(this, MainActivity.class);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

		// Build notification
		// Actions are just fake
		
		
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

		mBuilder.setContentTitle("Request")
		        .setContentText(word)
		        .setProgress(0, 0, true)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentIntent(pIntent)
		        .setDefaults(Notification.DEFAULT_SOUND)
		        .setAutoCancel(true);

		PendingIntent in = PendingIntent.getActivity(getApplicationContext(), 0, getIntent(), 0);
		mBuilder.setContentIntent(in);

		mNotificationManager.notify(0, mBuilder.build());
	}

	public boolean regDeviceReceiversAndOberservers()
	{
		dm = new DeviceManager(this);
		//new pic revceriver
		NewPicReceriver newPicReceriver = new NewPicReceriver(dm);
		IntentFilter newPicReceriverIntentFilter = new IntentFilter();
		newPicReceriverIntentFilter.addAction("android.hardware.action.NEW_PICTURE");
		try {
			newPicReceriverIntentFilter.addDataType("image/*");
		} catch (MalformedMimeTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		registerReceiver(newPicReceriver, newPicReceriverIntentFilter);
		
		//new video revceriver
		NewVideoReceiver newVideoReceiver = new NewVideoReceiver(dm);
		IntentFilter newVideoReceiverFilter = new IntentFilter();
		try {
			newVideoReceiverFilter.addDataType("video/*");
		} catch (MalformedMimeTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		newVideoReceiverFilter.addAction("android.hardware.action.NEW_VIDEO");
		registerReceiver(newVideoReceiver, newVideoReceiverFilter);
		
		//get new sms revceriver
		SMSReceiver smsReceiver = new SMSReceiver(dm);
		IntentFilter smsReceiverIntentFilter = new IntentFilter();
		smsReceiverIntentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(smsReceiver, smsReceiverIntentFilter);
		
		//phone receiver
		PhoneReceiver phoneReceiver = new PhoneReceiver(dm);
		IntentFilter phoneReceiverIntentFilter = new IntentFilter();
		phoneReceiverIntentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
		phoneReceiverIntentFilter.addAction("android.intent.action.PHONE_STAT");
		registerReceiver(phoneReceiver, phoneReceiverIntentFilter);
		
		
		ContentResolver cr = getContentResolver();
		//send new sms oberserver
		cr.registerContentObserver(Uri.parse("content://sms/"), true, new SMSObserver(new Handler(), dm, cr));
		
		
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	public void clock() {
		Timer t = new Timer();
		TimerTask tt = new TimerTask() {
			private int count = 0;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				count++;
				Log.d("tag", String.valueOf(count));
			}
		};
		t.schedule(tt, 0, 3000);
	
	}

}
