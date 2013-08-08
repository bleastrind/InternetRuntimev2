package com.internetruntime.androidclient.UI;

import android.util.Log;
import android.widget.LinearLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

public class UIMenuItemsClearListener implements com.nineoldandroids.animation.Animator.AnimatorListener,com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener
{
	private UIMenu menu = null;
	
	public UIMenuItemsClearListener(UIMenu menu) {
		// TODO Auto-generated constructor stub
		this.menu = menu;
	}
	
	@Override
	public void onAnimationUpdate(ValueAnimator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationCancel(Animator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animator arg0) {
		// TODO Auto-generated method stub
		if (menu != null)
			menu.setItemsInvisible();
	}

	@Override
	public void onAnimationRepeat(Animator arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animator arg0) {
		// TODO Auto-generated method stub
		
	}
	
}