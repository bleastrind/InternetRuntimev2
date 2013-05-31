package com.internetruntime.androidclient.UI;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class UIAnimeSet extends ValueAnimator
{
	private List<ValueAnimator> set = null;
	private AnimatorListener animatorListener = null;
	private int count;
	
	private int endAnimeCount;
	
	
	public UIAnimeSet(ObjectAnimator anime1, ObjectAnimator anime2)
	{
		// TODO Auto-generated constructor stub
		this.set = new ArrayList<ValueAnimator>();
		this.set.add(anime1);
		this.set.add(anime2);
		count = set.size();
	}
	
	public UIAnimeSet(List<ValueAnimator> set)
	{
		// TODO Auto-generated constructor stub
		this.set = set;
		count = set.size();
	}
	
	public void addListener(AnimatorListener animatorListener)
	{
		this.animatorListener = animatorListener;
	}
	
	public void setStartDelay(int dalay)
	{
		for (ValueAnimator a: set)
		{
			a.setStartDelay(dalay);
		}
	}
	
	private void endOne()
	{
		endAnimeCount++;
		if (endAnimeCount == count)
		{
			if (animatorListener != null)
				animatorListener.onAnimationEnd(null);
		}
	}
	
	@Override
	public void start()
	{
		Log.d("anime", "An anime set start with " + String.valueOf(set.size()));
		endAnimeCount = 0;
		for(ValueAnimator anime: set)
		{
			anime.addListener(new AnimatorListener()
			{				
				@Override
				public void onAnimationStart(Animator arg0)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator arg0)
				{
					// TODO Auto-generated method stub
					endOne();
				}
				
				@Override
				public void onAnimationCancel(Animator arg0)
				{
					// TODO Auto-generated method stub
					
				}
			});
			anime.start();
		}
	}
	
	@Override
	public void reverse()
	{
		Log.d("anime", "An anime set reverse with " + String.valueOf(set.size()));
		endAnimeCount = 0;
		for(ValueAnimator anime: set)
		{
			anime.addListener(new AnimatorListener()
			{				
				@Override
				public void onAnimationStart(Animator arg0)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationRepeat(Animator arg0)
				{
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onAnimationEnd(Animator arg0)
				{
					// TODO Auto-generated method stub
					endOne();
				}
				
				@Override
				public void onAnimationCancel(Animator arg0)
				{
					// TODO Auto-generated method stub
					
				}
			});
			anime.reverse();
		}
	}
	
	
	
	
	
	
}
