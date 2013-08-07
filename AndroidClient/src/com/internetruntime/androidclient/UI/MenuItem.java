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
	public TextView textView;
	
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
		LayoutParams layoutParams = new LayoutParams(UIConst.BTN_WIDTH, UIConst.BTN_HEIGHT);
		layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		
		textView.setLayoutParams(layoutParams);
		this.addView(textView);
		
		shadowCover = new ImageView(context);
		shadowCover.setImageResource(R.drawable.button_shadow);
		shadowCover.setLayoutParams(layoutParams);
//		this.addView(shadowCover);
		
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
	
	public void setLabelText(String text)
	{		
		textView.setText(text);
		textView.setTextSize(UIConst.BTN_TEXT_SIZE);
		textView.getPaint().setFakeBoldText(true);
		textView.setGravity(Gravity.CENTER_VERTICAL);
	}
	
	public void setMenuVisibility(int v)
	{
		setVisibility(VISIBLE);
	}
	
	
	public void setSubMenu(ListMenu subMenu)
	{
		this.subMenu = subMenu;
	}
	
	public void flyInLeft(int delay)
	{
		setMenuVisibility(VISIBLE);
		buttonFlyInInitLeft.start();
		UIAnimeSet animatorSet = new UIAnimeSet(buttonTextFadeInAnime, buttonFlyInLeftAnime);
		animatorSet.setStartDelay(delay);
		animatorSet.start();
	}
	
	public void flyInRight(int delay)
	{
		setMenuVisibility(VISIBLE);
		buttonFlyInInitRight.start();
		buttonFlyOutRightAnime.setStartDelay(delay);
		buttonFlyOutRightAnime.reverse();
	}
	
	public void flyOutLeft(int delay)
	{
		buttonFlyInRightAnime.setStartDelay(delay);
		buttonFlyInRightAnime.removeAllListeners();
		buttonFlyInRightAnime.addListener(new ClearItemListener(this));
		buttonFlyInRightAnime.reverse();
	}
	
	public void flyOutRight(int delay)
	{
		buttonFlyOutLeftAnime.setStartDelay(delay);
		buttonFlyOutLeftAnime.removeAllListeners();
		buttonFlyOutLeftAnime.addListener(new ClearItemListener(this));
		buttonFlyOutLeftAnime.start();
	}
	
	//button up
	private ObjectAnimator buttonBackgroundColorUpAnime;
	private ObjectAnimator buttonTextColorUpAnime;
	
	//text fade-in
	private ObjectAnimator buttonTextFadeInAnime;
	
	//fly inits
	private ObjectAnimator buttonFlyInInitLeft;
	private ObjectAnimator buttonFlyInInitRight;
	
	//fly-in
	private ObjectAnimator buttonFlyInLeftAnime;
	private ObjectAnimator buttonFlyInRightAnime;
	
	//fly-out
	private ObjectAnimator buttonFlyOutLeftAnime;
	private ObjectAnimator buttonFlyOutRightAnime;
	
	
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
		buttonFlyInLeftAnime = AnimeGenerator.genButtonFlyInAnime(this);
		buttonFlyInRightAnime = AnimeGenerator.genButtonFlyInAnime(this);
		
		//fly-out
		buttonFlyOutLeftAnime = AnimeGenerator.genButtonFlyOutAnime(this);
		buttonFlyOutRightAnime = AnimeGenerator.genButtonFlyOutAnime(this);
		
		
		
	}
	
	public class ClickListener implements OnClickListener{

		@Override
		public void onClick(View v)
		{
			if (subMenu != null)
			{
					parentMenu.goToMenu(subMenu, parentMenuIndex);
			}
		}
		
	}
	
	public class TapListner implements OnTouchListener{
		private int flagEvent = -1;
	
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
			
			return false;
		}
		
		
	}
	
	public void setTapAnime()
	{
		
		textView.setOnTouchListener(new TapListner());
		textView.setOnClickListener(new ClickListener());
	}

}
