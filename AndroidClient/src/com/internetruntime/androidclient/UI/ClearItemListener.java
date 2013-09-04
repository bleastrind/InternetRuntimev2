package com.internetruntime.androidclient.UI;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;

public class ClearItemListener implements AnimatorListener
{
	private MenuItem item;
	
	public ClearItemListener(MenuItem item)
	{
		this.item = item;
	}
	
	@Override
	public void onAnimationCancel(Animator arg0)
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animator arg0)
	{
		item.setVisibility(MenuItem.INVISIBLE);
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
