package com.internetruntime.androidclient.UI;

import android.content.Context;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class LableTextItem extends MenuItem{

	public LableTextItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutParams layoutParams = new LayoutParams(UIConst.BTN_WIDTH, UIConst.LABEL_TEXT_HEIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		textView.setLayoutParams(layoutParams);
	}
	
	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		textView.setText(text);
		textView.setTextSize(UIConst.BTN_TEXT_SIZE);
		textView.getPaint().setFakeBoldText(true);
		
		textView.setGravity(Gravity.CENTER_HORIZONTAL);
	}

}
