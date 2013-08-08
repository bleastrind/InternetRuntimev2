package com.internetruntime.androidclient.UI;

import java.util.Timer;
import java.util.TimerTask;

import com.example.internetruntime.androidclient.R;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ArgbEvaluator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.animation.AnimatorProxy;


import android.R.drawable;
import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class UIMenuButtonItem{
	private TextView textView = null;
	private UIMenuButtonItem thisObject = this;
	
	public UIMenuButtonItem(Context context) {
		// TODO Auto-generated constructor stub
		activity = (UIActivity)context;
		textView = new TextView(context);
		textView.setPadding(0, 0, 0, 0);	
		textView.setBackgroundColor(UIConst.BTN_BACKGROUNG_UP);
//		textView.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.button_shadow_test));
		
		textView.setTextColor(UIConst.BTN_TEXT_UP_UNVISIBLE);	
//		textView.setShadowLayer(1, 0, -1, 0xff000000);
		
		textView.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					//Touch down: no anime for fast react
					textView.setBackgroundColor(UIConst.BTN_BACKGROUNG_DOWN);
					textView.setTextColor(UIConst.BTN_TEXT_DOWN);
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) 
				{
					//Touch up: color fade change anime
					ObjectAnimator backgroungAnime = UIAnimeGenerator.genButtonBackgroundColorUpAnime(thisObject);
					ObjectAnimator textAnime = UIAnimeGenerator.genButtonTextColorUpAnime(thisObject);
					//Run anime set.
					AnimatorSet set = new AnimatorSet();
					set.playTogether(backgroungAnime, textAnime);
					set.setDuration(UIConst.BTN_COLOR_FADE_DURATION);
					set.start();
				}
				return false;
			}
		});
	}

	private int width = 0;
	private int height = 0;
	private int backgroundColor = 0;
	private String text = null;
	private UIMenu relativeMenu = null;
	private UIActivity activity = null;
	
	public ValueAnimator getHideAnime(int delay) {
		//Set button move-out anime with delay.	
		ObjectAnimator buttonMoveOutAnime = UIAnimeGenerator.genButtonMoveOutAnime(this);
		buttonMoveOutAnime.setStartDelay(delay);
		
		return buttonMoveOutAnime;		
	}
	
	public ValueAnimator getHideReverseAnime(int delay)
	{
		// Init button start x of move-out reverse anime.		
		ObjectAnimator buttonMoveInInit = UIAnimeGenerator.genButtonMoveOutReverseInit(this);
		buttonMoveInInit.start();
		//Set button move-out anime with delay.	
		ObjectAnimator buttonMoveOutAnime = UIAnimeGenerator.genButtonMoveOutAnime(this);
		buttonMoveOutAnime.setStartDelay(delay);
		
		return buttonMoveOutAnime;		
	}
	
	public ValueAnimator getStartAnime(int delay)
	{
//		//Init button start x
//		ObjectAnimator buttonMoveInInit = UIAnimeGenerator.genButtonMoveInInit(this);	
//		buttonMoveInInit.start();
		//Init button start scaleX
		ObjectAnimator buttonScaleXInit = UIAnimeGenerator.genButtonScaleXInit(this);
		buttonScaleXInit.start();	
		//Set button scaleX anime.
		ObjectAnimator buttonScaleXAnime = UIAnimeGenerator.genButtonScaleXAnime(this);
		buttonScaleXAnime.setStartDelay(delay);	
		//Set text fade-in anime.
		ObjectAnimator buttonTextFadeInAnime = UIAnimeGenerator.genButtonTextFadeInAnime(this);
		buttonTextFadeInAnime.setStartDelay(delay);	
		//Run anime set.
		UIAnimeSet animatorSet = new UIAnimeSet(buttonScaleXAnime, buttonTextFadeInAnime);
		return animatorSet;		
	}
	
	public ValueAnimator getShowAnime(int delay)
	{
		//Init button start x of move-in anime.
		ObjectAnimator buttonMoveInInit = UIAnimeGenerator.genButtonMoveInInit(this);	
		buttonMoveInInit.start();
		//Set button move-in anime with delay.
		ObjectAnimator buttonMoveInAnime = UIAnimeGenerator.genButtonMoveInAnime(this);	
		buttonMoveInAnime.setStartDelay(delay);	
		//Set text fade-in anime.
		ObjectAnimator buttonTextFadeInAnime = UIAnimeGenerator.genButtonTextFadeInAnime(this);
		//Run anime set.
		UIAnimeSet animatorSet = new UIAnimeSet(buttonMoveInAnime, buttonTextFadeInAnime);
		return animatorSet;		
	}
	
	public ValueAnimator getShowReverseAnime(int delay)
	{
		//Set button move-in anime with delay.
		ObjectAnimator buttonMoveInAnime = UIAnimeGenerator.genButtonMoveInAnime(this);	
		buttonMoveInAnime.setStartDelay(delay);	
		
		return buttonMoveInAnime;
	}
	
	
	public void setRelativeMenu(UIMenu relativeMenu) {
		this.relativeMenu = relativeMenu;
	}
	
	public UIMenu getRelativeMenu() {
		return relativeMenu;
	}
	
	public void showRelativeMenu(RandomSeq randomSeq)
	{
		if (relativeMenu != null)
		{
			relativeMenu.render(activity.getMainLayout());
			relativeMenu.show(randomSeq, UIConst.MENU_SHOW_NEW_DELAY, false);
		}
	}
	
	public void setClickListener(OnClickListener l)
	{
		textView.setOnClickListener(l);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getBackgroundColor() {
		return backgroundColor;
	}
	
	public String getText() {
		return text;
	}
	
	public void setHeight(int height) {
		this.height = height;
		textView.setLayoutParams(new LayoutParams(width, height));		
	}
	
	public void setWidth(int width) {
		this.width = width;
		textView.setLayoutParams(new LayoutParams(width, height));		
	}
	
	public void setBackgroundColor(int backgroundColor) {
		textView.setBackgroundColor(backgroundColor);
		this.backgroundColor = backgroundColor;
	}
	
	public void setText(String text) {
		textView.setText(text);
		textView.setTextSize(UIConst.BTN_TEXT_SIZE);
		textView.getPaint().setFakeBoldText(true);
		textView.setGravity(Gravity.CENTER);
		this.text = text;
	}
	
	public View getView() {
		return textView;
	}
}
