package com.internetruntime.androidclient.UI;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;

public class AnimeGenerator
{
	//button up - background color
	public static final ObjectAnimator genButtonBackgroundColorUpAnime(View button)
	{
		ObjectAnimator buttonBackgroundColorUpAnime = ObjectAnimator.ofInt(
				button,
				"backgroundColor",
				UIConst.BTN_BACKGROUNG_DOWN,
				UIConst.BTN_BACKGROUNG_UP);
		buttonBackgroundColorUpAnime.setEvaluator(new ArgbEvaluator());
		return buttonBackgroundColorUpAnime;
	}
	
	//button up - text color
	public static final ObjectAnimator genButtonTextColorUpAnime(View button)
	{
		ObjectAnimator buttonTextColorUpAnime = ObjectAnimator.ofInt(
				button,
				"textColor",
				UIConst.BTN_TEXT_DOWN,
				UIConst.BTN_TEXT_UP);
		buttonTextColorUpAnime.setEvaluator(new ArgbEvaluator());
		return buttonTextColorUpAnime;
	}
	
	//button text fade-in
	public static final ObjectAnimator genButtonTextFadeInAnime(View button)
	{
		ObjectAnimator buttonTextFadeInAnime = ObjectAnimator.ofInt(
				button,
				"textColor",
				UIConst.BTN_TEXT_UP_UNVISIBLE,
				UIConst.BTN_TEXT_UP_MID_1);
		buttonTextFadeInAnime.setEvaluator(new ArgbEvaluator());
		buttonTextFadeInAnime.setDuration(UIConst.BTN_TEXT_SHOW_TIME_HALF);
		ColorFadeInListener colorFadeInListener = new ColorFadeInListener(button);
		buttonTextFadeInAnime.addListener(colorFadeInListener);
		return buttonTextFadeInAnime;
	}
	
	//button fly-in init left
	public static final ObjectAnimator genButtonFlyInInitLeft(View button)
	{
		ObjectAnimator buttonFlyInInit = ObjectAnimator.ofFloat(
				button, 
				"translationX", 
				UIConst.BTN_FADE_IN_START_X);
		buttonFlyInInit.setDuration(0);
		return buttonFlyInInit;		
	}
	
	//button fly-in init right
	public static final ObjectAnimator genButtonFlyInInitRight(View button)
	{
		ObjectAnimator buttonFlyInInit = ObjectAnimator.ofFloat(
				button, 
				"translationX", 
				UIConst.BTN_FADE_OUT_END_X);
		buttonFlyInInit.setDuration(0);
		return buttonFlyInInit;		
	}
	
	//button fly-in 
	public static final ObjectAnimator genButtonFlyInAnime(View button)
	{
		ObjectAnimator buttonFlyInAnime = ObjectAnimator.ofFloat(
				button, 
				"translationX", 
				UIConst.BTN_FADE_IN_START_X,
				UIConst.BTN_X);
		buttonFlyInAnime.setDuration(UIConst.BTN_MOVE_DURATION);
		buttonFlyInAnime.setInterpolator(new OvershootInterpolator(4));
		return buttonFlyInAnime;		
	}
	
	//button fly-out
	public static final ObjectAnimator genButtonFlyOutAnime(View button)
	{
		ObjectAnimator buttonFlyOutAnime = ObjectAnimator.ofFloat(
				button, 
				"translationX", 
				UIConst.BTN_X,
				UIConst.BTN_FADE_OUT_END_X);
		buttonFlyOutAnime.setDuration(UIConst.BTN_MOVE_DURATION);
		buttonFlyOutAnime.setInterpolator(new AccelerateInterpolator(3));
		return buttonFlyOutAnime;		
	}
	
	
}
