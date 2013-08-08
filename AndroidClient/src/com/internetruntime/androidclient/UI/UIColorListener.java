package com.internetruntime.androidclient.UI;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class UIColorListener implements com.nineoldandroids.animation.Animator.AnimatorListener,com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener
{
	private UIMenuButtonItem button;
	
	public UIColorListener(UIMenuButtonItem button) {
		// TODO Auto-generated constructor stub
		this.button = button;
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
		ObjectAnimator objectAnimator = ObjectAnimator.ofInt(button.getView(),
				"textColor", UIConst.BTN_TEXT_UP_MID_2,
				UIConst.BTN_TEXT_UP);
		objectAnimator.setEvaluator(new ArgbEvaluator());
		objectAnimator.setDuration(UIConst.BTN_TEXT_SHOW_TIME_HALF);
		objectAnimator.start();
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
