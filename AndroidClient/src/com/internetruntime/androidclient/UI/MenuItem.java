package com.internetruntime.androidclient.UI;

import com.example.internetruntime.androidclient.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MenuItem extends RelativeLayout
{
	private int id;
	
	private Context context;
	private TextView textView;
	
	private ImageView shadowCover;
	
	private ListMenu parentMenu;
	private int parentMenuIndex;
	private ListMenu subMenu;

	public MenuItem(Context context)
	{
		super(context);
		this.context = context;
		
		id = UIConst.ID_COUNT++;
		setId(id);
		
		textView = new TextView(context);
		textView.setBackgroundColor(UIConst.BTN_BACKGROUNG_UP);
		textView.setTextColor(UIConst.BTN_TEXT_UP_UNVISIBLE);	
		textView.setLayoutParams(new LayoutParams(UIConst.BTN_WIDTH, UIConst.BTN_HEIGHT));
		this.addView(textView);
		
		shadowCover = new ImageView(context);
		shadowCover.setImageResource(R.drawable.button_shadow);
		shadowCover.setLayoutParams(new LayoutParams(UIConst.BTN_WIDTH, UIConst.BTN_HEIGHT));
		this.addView(shadowCover);
		
		this.setVisibility(GONE);
		
		genAnimes();
		setTapAnime();
	}
	
	public void setVisibility(int visibility)
	{
		textView.setVisibility(visibility);
		shadowCover.setVisibility(visibility);
	}

	
	public void setParentMenu(ListMenu parentMenu)
	{
		this.parentMenu = parentMenu;
		this.parentMenuIndex = parentMenu.indexOfChild(this);
	}
	
	public int getId()
	{
		return id;
	}
	
	public void setText(String text)
	{
		textView.setText(text);
		textView.setTextSize(UIConst.BTN_TEXT_SIZE);
		textView.getPaint().setFakeBoldText(true);
		textView.setGravity(Gravity.CENTER);
	}
	
	
	public void setSubMenu(ListMenu subMenu)
	{
		this.subMenu = subMenu;
	}
	
	public void flyInLeft(int delay)
	{
		setVisibility(VISIBLE);
		buttonFlyInInitLeft.start();
		UIAnimeSet animatorSet = new UIAnimeSet(buttonTextFadeInAnime, buttonFlyInAnime);
		animatorSet.setStartDelay(delay);
		animatorSet.start();
	}
	
	public void flyInRight(int delay)
	{
		setVisibility(VISIBLE);
		buttonFlyInInitRight.start();
		buttonFlyOutAnime.setStartDelay(delay);
		buttonFlyOutAnime.reverse();
	}
	
	public void flyOutLeft(int delay)
	{
		buttonFlyInAnime.setStartDelay(delay);
		buttonFlyInAnime.removeAllListeners();
		buttonFlyInAnime.addListener(new ClearItemListener(this));
		buttonFlyInAnime.reverse();
	}
	
	public void flyOutRight(int delay)
	{
		buttonFlyOutAnime.setStartDelay(delay);
		buttonFlyOutAnime.removeAllListeners();
		buttonFlyOutAnime.addListener(new ClearItemListener(this));
		buttonFlyOutAnime.start();
	}
	
	//button up
	private ObjectAnimator buttonBackgroundColorUpAnime;
	private ObjectAnimator buttonTextColorUpAnime;
	
	//text fade-in
	private ObjectAnimator buttonTextFadeInAnime;
	
	//fly inits
	private ObjectAnimator buttonFlyInInitLeft;
	private ObjectAnimator buttonFlyInInitRight;
	
	//fly-in left
	private ObjectAnimator buttonFlyInAnime;
	
	//fly-out
	private ObjectAnimator buttonFlyOutAnime;
	
	public void genAnimes()
	{
		//button up
		buttonBackgroundColorUpAnime = AnimeGenerator.genButtonBackgroundColorUpAnime(textView);
		buttonTextColorUpAnime = AnimeGenerator.genButtonTextColorUpAnime(textView);
		
		//text fade-in
		buttonTextFadeInAnime = AnimeGenerator.genButtonTextFadeInAnime(textView);
		
		//fly inits
		buttonFlyInInitLeft = AnimeGenerator.genButtonFlyInInitLeft(this);
		buttonFlyInInitRight = AnimeGenerator.genButtonFlyInInitRight(this);
		
		//fly-in 
		buttonFlyInAnime = AnimeGenerator.genButtonFlyInAnime(this);
		
		//fly-out
		buttonFlyOutAnime = AnimeGenerator.genButtonFlyOutAnime(this);
		
		
		
		
	}
	
	public void setTapAnime()
	{
		this.setOnTouchListener(new OnTouchListener() {			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN)
				{
					//Touch down: no anime for fast react
					Log.d("ui", "action downnnnnn");
					textView.setBackgroundColor(UIConst.BTN_BACKGROUNG_DOWN);
					textView.setTextColor(UIConst.BTN_TEXT_DOWN);
				}
				else if (event.getAction() == MotionEvent.ACTION_UP) 
				{
					Log.d("ui", "action uppppppp");
					AnimatorSet set = new AnimatorSet();
					set.playTogether(buttonBackgroundColorUpAnime, buttonTextColorUpAnime);
					set.setDuration(UIConst.BTN_COLOR_FADE_DURATION);
					set.start();
				}
				if (subMenu != null)
				{
					parentMenu.goToMenu(subMenu, parentMenuIndex);
				}
				
				return true;
			}
		});
	}

}
