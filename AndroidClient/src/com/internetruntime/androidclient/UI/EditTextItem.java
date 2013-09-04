package com.internetruntime.androidclient.UI;

import android.content.Context;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

public class EditTextItem extends MenuItem{
	private EditText editText;
	
	public EditTextItem(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		editText = new EditText(context);
		
		LayoutParams layoutParams = new LayoutParams(UIConst.BTN_WIDTH, UIConst.LABEL_TEXT_HEIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		textView.setLayoutParams(layoutParams);
		
		layoutParams = new LayoutParams(UIConst.EDIT_TEXT_WIDTH, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		layoutParams.setMargins(0, 0, 0, 0);
		
		
		editText.setLayoutParams(layoutParams);
		editText.setMaxLines(1);
		this.addView(editText);
		
		setText("");	
		editText.setVisibility(GONE);
		
	}
	
	@Override
	public void setMenuVisibility(int v) {
		// TODO Auto-generated method stub
		super.setMenuVisibility(v);
		editText.setVisibility(VISIBLE);
	}
	
	public void setPassword()
	{
		editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
	}

}
