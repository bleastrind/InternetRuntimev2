package com.internetruntime.androidclient.UI;

import android.util.Log;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;



import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;

public class UIAnimeGenerator {
	
	//Button scale X Init
	public static final ObjectAnimator genButtonScaleXInit(UIMenuButtonItem button)
	{
		ObjectAnimator buttonMoveInAnime = ObjectAnimator.ofFloat(
				button.getView(), 
				"scaleX", 
				(float)UIConst.BTN_SCALE_START);
		buttonMoveInAnime.setDuration(0);
		return buttonMoveInAnime;		
	}
	
	//Button scale X
	public static final ObjectAnimator genButtonScaleXAnime(UIMenuButtonItem button)
	{
		ObjectAnimator buttonMoveInAnime = ObjectAnimator.ofFloat(
				button.getView(), 
				"scaleX", 
				(float)UIConst.BTN_SCALE_START,
				(float)UIConst.BTN_SCALE_END);
		buttonMoveInAnime.setDuration(UIConst.BTN_SCALE_DURATION);
		buttonMoveInAnime.setInterpolator(new OvershootInterpolator(4));
		return buttonMoveInAnime;		
	}
	
	
	//Button move out init
	public static final ObjectAnimator genButtonMoveOutInit(UIMenuButtonItem button)
	{
		ObjectAnimator buttonMoveOutInit = ObjectAnimator.ofFloat(
				button.getView(), 
				"translationX", 
				UIConst.BTN_X);
		buttonMoveOutInit.setDuration(0);
		return buttonMoveOutInit;		
	}
	
	//Button move out reverse init
	public static final ObjectAnimator genButtonMoveOutReverseInit(UIMenuButtonItem button)
	{
		ObjectAnimator buttonMoveOutReverseInit = ObjectAnimator.ofFloat(
				button.getView(), 
				"translationX", 
				UIConst.BTN_FADE_OUT_END_X);
		buttonMoveOutReverseInit.setDuration(0);
		return buttonMoveOutReverseInit;		
	}
	
	//Button move out
	public static final ObjectAnimator genButtonMoveOutAnime(UIMenuButtonItem button)
	{
		ObjectAnimator buttonMoveOutAnime = ObjectAnimator.ofFloat(
				button.getView(), 
				"translationX", 
				UIConst.BTN_X,
				UIConst.BTN_FADE_OUT_END_X);
		buttonMoveOutAnime.setDuration(UIConst.BTN_MOVE_DURATION);
		buttonMoveOutAnime.setInterpolator(new AccelerateInterpolator(3));
		return buttonMoveOutAnime;		
	}
	
	//Button move in init
	public static final ObjectAnimator genButtonMoveInInit(UIMenuButtonItem button)
	{
		ObjectAnimator buttonMoveInInit = ObjectAnimator.ofFloat(
				button.getView(), 
				"translationX", 
				UIConst.BTN_FADE_IN_START_X);
		buttonMoveInInit.setDuration(0);
		return buttonMoveInInit;		
	}
	
	
	//Button move in
	public static final ObjectAnimator genButtonMoveInAnime(UIMenuButtonItem button)
	{
		ObjectAnimator buttonMoveInAnime = ObjectAnimator.ofFloat(
				button.getView(), 
				"translationX", 
				UIConst.BTN_FADE_IN_START_X,
				UIConst.BTN_X);
		buttonMoveInAnime.setDuration(UIConst.BTN_MOVE_DURATION);
		buttonMoveInAnime.setInterpolator(new OvershootInterpolator(4));
		return buttonMoveInAnime;		
	}
	
	
	//Button text fade in
	public static final ObjectAnimator genButtonTextFadeInAnime(UIMenuButtonItem button)
	{
		ObjectAnimator buttonTextFadeInAnime = ObjectAnimator.ofInt(
				button.getView(),
				"textColor",
				UIConst.BTN_TEXT_UP_UNVISIBLE,
				UIConst.BTN_TEXT_UP_MID_1);
		buttonTextFadeInAnime.setEvaluator(new ArgbEvaluator());
		buttonTextFadeInAnime.setDuration(UIConst.BTN_TEXT_SHOW_TIME_HALF);
		UIColorListener ml = new UIColorListener(button);
//		buttonTextFadeInAnime.addUpdateListener(ml);
		buttonTextFadeInAnime.addListener(ml);
		return buttonTextFadeInAnime;
	}
	
	//Button up background color fade change
	public static final ObjectAnimator genButtonBackgroundColorUpAnime(UIMenuButtonItem button)
	{
		ObjectAnimator buttonBackgroundColorUpAnime = ObjectAnimator.ofInt(
				button.getView(),
				"backgroundColor",
				UIConst.BTN_BACKGROUNG_DOWN,
				UIConst.BTN_BACKGROUNG_UP);
		buttonBackgroundColorUpAnime.setEvaluator(new ArgbEvaluator());
		return buttonBackgroundColorUpAnime;
	}
	
	//Button up text color fade change
	public static final ObjectAnimator genButtonTextColorUpAnime(UIMenuButtonItem button)
	{
		ObjectAnimator buttonTextColorUpAnime = ObjectAnimator.ofInt(
				button.getView(),
				"textColor",
				UIConst.BTN_TEXT_DOWN,
				UIConst.BTN_TEXT_UP);
		buttonTextColorUpAnime.setEvaluator(new ArgbEvaluator());
		return buttonTextColorUpAnime;
	}
	
		
	
}
