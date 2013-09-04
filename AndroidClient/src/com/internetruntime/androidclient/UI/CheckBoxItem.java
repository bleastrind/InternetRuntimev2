package com.internetruntime.androidclient.UI;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class CheckBoxItem extends MenuItem{
	private CheckBox checkBox;

	public CheckBoxItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		checkBox = new CheckBox(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.setMargins(0, 0, 160, 0);
		checkBox.setLayoutParams(layoutParams);
		addView(checkBox);
		checkBox.setVisibility(GONE);
	}
	
	public void setMenuVisibility(int v) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(v);
		checkBox.setVisibility(VISIBLE);
	}

}
