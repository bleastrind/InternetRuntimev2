package com.internetruntime.androidclient.UI;

import android.app.Activity;
import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class UILogField
{
	private EditText editText = null;
	private UIActivity activity = null;
	private int width;
	private int height;
	
	public UILogField(Context context)
	{
		// TODO Auto-generated constructor stub
		this.editText = new EditText(context);
		this.activity = (UIActivity)context;
		width = (int)(UIConst.SCREEN_WIDTH * 0.6);
		height = (int)(UIConst.SCREEN_HEIGHT * 0.6);
		this.editText.setLayoutParams(new LayoutParams(width, height));
	}
	
	public void render()
	{
		//Render the menu
		if (!isRendered())
		{
			//Set paddings and margins
			RelativeLayout mainLayout = activity.getMainLayout();
			mainLayout.setPadding(0, 0, 0, 0);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(0, 0, 0, 0);
			
			//Add the menu laytou
			mainLayout.addView(editText, layoutParams);			
		}
		
		
	}
	
	private boolean isRendered()
	{
		if (activity.getMainLayout().indexOfChild(editText) >= 0)
			return true;
		else
			return false;	 
	}
}
