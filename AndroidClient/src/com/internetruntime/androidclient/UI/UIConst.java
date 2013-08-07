package com.internetruntime.androidclient.UI;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class UIConst {
	
	static public int ID_COUNT = 0;
	
	static public int SCREEN_HEIGHT;
	static public int SCREEN_WIDTH;
	
	static public final int EDIT_TEXT_WIDTH = 300;
	static public final int LABEL_TEXT_HEIGHT = 90;
	

	static public final int LIST_MENU_CHANGE_INTERVAL = 500;
	
	static public final int BTN_WIDTH = 500;
	static public final int BTN_HEIGHT = 120;
	static public final int BTN_FADE_IN_START_X = 0 - BTN_WIDTH - 200;	
	static public final int BTN_X = 0;
	static public int BTN_FADE_OUT_END_X;
	
	
	static public final int BTN_BACKGROUNG_UP = 0xff696969;
	static public final int BTN_BACKGROUNG_DOWN = 0xff68228B;
	static public final int BTN_TEXT_UP_UNVISIBLE = 0x00000099;
	static public final int BTN_TEXT_UP_MID_1 = 0x79000099;
	static public final int BTN_TEXT_UP_MID_2 = 0x80000099;
	static public final int BTN_TEXT_UP = 0xff000099;
	static public final int BTN_TEXT_DOWN = 0xff76EE00;
	static public final int BTN_TEXT_SIZE = 35;
	static public final int BTN_TEXT_SHOW_TIME_HALF = 700;
	static public final int BTN_MOVE_DURATION = 600;
	static public final int BTN_COLOR_FADE_DURATION = 400;
	static public final int BTN_FADE_INTERVAL = 100;	//interval between 2 button anime
	
	static public final double BTN_SCALE_START = 0.8;
	static public final double BTN_SCALE_END = 1;
	static public final int BTN_SCALE_DURATION = 400;
	
	static public final int MENU_SHOW_NEW_DELAY = 500;
	
	static public final int CONSOLE_WIDTH = 200;
	static public final int CONSOLE_HEIGHT = 200;
	static public final int CONSOLE_BACKGROUNG = 0xff696969;
	
	static public void Init(Context context)
	{
		Activity activity = (Activity)context;
		WindowManager manage = activity.getWindowManager();
	    Display display = manage.getDefaultDisplay();
	    DisplayMetrics metric = new DisplayMetrics();
	    display.getMetrics(metric);	
	    UIConst.SCREEN_HEIGHT = metric.heightPixels; 
	    UIConst.SCREEN_WIDTH = metric.widthPixels;
	    
	    UIConst.BTN_FADE_OUT_END_X = UIConst.SCREEN_WIDTH;
	}
}
