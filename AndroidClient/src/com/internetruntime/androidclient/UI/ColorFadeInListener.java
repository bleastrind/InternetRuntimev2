package com.internetruntime.androidclient.UI;

import android.view.View;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;

public class ColorFadeInListener implements com.nineoldandroids.animation.Animator.AnimatorListener
{
	private View button;
	
	public ColorFadeInListener(View button)
	{
		this.button = button;
	}
	
	@Override
	public void onAnimationCancel(Animator arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animator arg0)
	{
		ObjectAnimator objectAnimator = ObjectAnimator.ofInt(button,
				"textColor", UIConst.BTN_TEXT_UP_MID_2,
				UIConst.BTN_TEXT_UP);
		objectAnimator.setEvaluator(new ArgbEvaluator());
		objectAnimator.setDuration(UIConst.BTN_TEXT_SHOW_TIME_HALF);
		objectAnimator.start();
	}
	

	@Override
	public void onAnimationRepeat(Animator arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animator arg0)
	{
		// TODO Auto-generated method stub
		
	}

}
