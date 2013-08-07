package com.internetruntime.androidclient.UI;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class RadioTextItem extends MenuItem{
	private RadioButton radioButton;
	
	public RadioTextItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		radioButton =  new RadioButton(context);
		LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layoutParams.setMargins(0, 0, 160, 0);
		radioButton.setLayoutParams(layoutParams);
		addView(radioButton);
		radioButton.setVisibility(GONE);
	}
	public void setMenuVisibility(int v) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(v);
		radioButton.setVisibility(VISIBLE);
	}

	
}
