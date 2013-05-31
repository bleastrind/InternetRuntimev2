package com.internetruntime.androidclient.UI;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class UIConsole extends TextView{
	
	private UIActivity activity = null;
	
	private int width = 0;
	private int height = 0;
	private int backgroundColor = 0;
	
	public UIConsole(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		activity = (UIActivity)context;
	}
	
	public void setHeight(int height) {
		this.height = height;		
		this.setLayoutParams(new LayoutParams(width, height));
		
	}
	
	public void setWidth(int width) {
		this.width = width;
		this.setLayoutParams(new LayoutParams(width, height));		
	}
	
	public void setBackground(int backgroundColor) {
		this.backgroundColor = backgroundColor;
		this.setBackgroundColor(backgroundColor);		
	}
	
	public void render(ViewGroup group) {
		if (!isRenderedBy(group))
		{
			//Set paddings and margins
			group.setPadding(0, 0, 0, 0);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, height);
			layoutParams.setMargins(0, 0, 0, 0);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			
			//Add the menu laytou
			group.addView(this, layoutParams);			
		}
	}
	
	private boolean isRenderedBy(ViewGroup group)
	{
		if (group.indexOfChild(this) >= 0)
			return true;
		else
			return false;	 
	}
}
