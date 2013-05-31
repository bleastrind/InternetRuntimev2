package com.internetruntime.androidclient.UI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.client.CircularRedirectException;

import static com.nineoldandroids.view.ViewPropertyAnimator.animate;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.Animator;

import android.R.bool;
import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Toast;


public class UIMenu extends LinearLayout implements UIGeneral{
	
	private List<UIMenuButtonItem> items = new ArrayList();
	private UIActivity activity = null;
		
	public UIMenu(Context context) {
		
		//Set paddings margins and orientation
		super(context);
		activity = (UIActivity) context;
		this.setPadding(0, 0, 0, 0);
		this.setOrientation(VERTICAL);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);  
		layoutParams.setMargins(0, 0, 0, 0);
		this.setLayoutParams(layoutParams);
	}

	public int getSize() {
		return items.size();
	}
	
	public int indexOf(UIMenuButtonItem item)
	{
		return items.indexOf(item);
	}
	
	public UIMenuButtonItem get(int i)
	{
		return items.get(i);
	}
	
	public UIMenu addItem(UIMenuButtonItem item) {
		items.add(item);
		return this;
	} 
	
	public void setItemsInvisible()
	{
		this.removeAllViews();
	}
	
	public void showSubMenu(int index)
	{	
		UIMenuButtonItem item = items.get(index); 
		UIMenu subMenu = item.getRelativeMenu();
		if (subMenu != null)
		{
			RandomSeqGenerator randomIndexGenerater = new RandomSeqGenerator(subMenu.getSize());
			RandomSeq randomSeq = randomIndexGenerater.getRandomSeqStartWith(index);
			hide(randomSeq, 0, false);
			item.showRelativeMenu(randomSeq);			
//			activity.setCurrentMenu(subMenu);
		}
	}
	
	public boolean hide(RandomSeq seq, int pubDelay, boolean backFlag) {
		//default start seq
		RandomSeq randomSeq;
		if (seq == null)
		{
			RandomSeqGenerator randomIndexGenerater = new RandomSeqGenerator(items.size());
			randomSeq = randomIndexGenerater.getRandomSeq();
		}
		else
			randomSeq = seq;
		
	    //Anime set of all button animes		
		List<ValueAnimator> animators = new ArrayList<ValueAnimator>();
		for (UIMenuButtonItem item: items)
		{
			int itemIndex = items.indexOf(item);
			int seqIndex = randomSeq.indexOf(itemIndex);
			int delay = seqIndex * UIConst.BTN_FADE_INTERVAL;
			if (backFlag)
				animators.add(item.getShowReverseAnime(delay + pubDelay));
			else
				animators.add(item.getHideAnime(delay + pubDelay));
		}
		UIAnimeSet animatorSet = new UIAnimeSet(animators);
		
		//Add listener: clear views after all animes ends
		UIMenuItemsClearListener listener = new UIMenuItemsClearListener(this);
		animatorSet.addListener(listener);
		
		//Start animes
		if (backFlag)
			animatorSet.reverse();
		else
			animatorSet.start();
		return true;
	}
	
	
	public boolean start(RandomSeq seq, int pubDelay)
	{
		//default start seq
		RandomSeq randomSeq;
		if (seq == null)
		{
			RandomSeqGenerator randomIndexGenerater = new RandomSeqGenerator(items.size());
			randomSeq = randomIndexGenerater.getRandomSeq();
		}
		else
			randomSeq = seq;		
		
		// Anime set of all button animes		
		List<ValueAnimator> animators = new ArrayList<ValueAnimator>();
		for (UIMenuButtonItem item : items) 
		{
			int itemIndex = items.indexOf(item);
			int seqIndex = randomSeq.indexOf(itemIndex);
			int delay = seqIndex * UIConst.BTN_FADE_INTERVAL;
//			animators.add(item.getStartAnime(delay + pubDelay));

			Animation anime = new ScaleAnimation((float)0.9, (float)1.0, (float)0.9, (float)1.0, (float)0.5, (float)0.5);
			anime.setDuration(400);
			anime.setStartOffset(delay + pubDelay);
			anime.setInterpolator(new OvershootInterpolator(4));
			
			item.getView().startAnimation(anime);
		}
//		UIAnimeSet animatorSet = new UIAnimeSet(animators);
//
////		// Start animes
//		animatorSet.start();
		return true;
	}
		
	public boolean show(RandomSeq seq, int pubDelay, boolean backFlag)
	{
		//default start seq
		RandomSeq randomSeq;
		if (seq == null)
		{
			RandomSeqGenerator randomIndexGenerater = new RandomSeqGenerator(items.size());
			randomSeq = randomIndexGenerater.getRandomSeq();
		}
		else
			randomSeq = seq;

		// Anime set of all button animes		
		List<ValueAnimator> animators = new ArrayList<ValueAnimator>();
		for (UIMenuButtonItem item : items) 
		{
			int itemIndex = items.indexOf(item);
			int seqIndex = randomSeq.indexOf(itemIndex);
			int delay = seqIndex * UIConst.BTN_FADE_INTERVAL;
			if (backFlag)
				animators.add(item.getHideReverseAnime(delay + pubDelay));
			else
				animators.add(item.getShowAnime(delay + pubDelay));
		}
		UIAnimeSet animatorSet = new UIAnimeSet(animators);

		// Start animes
		if (backFlag)
			animatorSet.reverse();
		else
			animatorSet.start();
		return true;
	}
	
	public boolean render(ViewGroup group)
	{
		//Render the menu
		if (!isRenderedBy(group))
		{
			//Set paddings and margins
			group.setPadding(0, 0, 0, 0);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			layoutParams.setMargins(0, 0, 0, 0);
			
			//Add the menu laytou
			group.addView(this, layoutParams);			
		}
		
		//Render items of the menu
		for (UIMenuButtonItem item: items)
		{
			//Set items marings
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					item.getWidth(), item.getHeight());
			layoutParams.setMargins(0, 0, 0, 0);
			
			//Add view to layout
			if (!hasItem(item))
				this.addView(item.getView(), layoutParams);
		}
		return true;
	}
	
	
	
	

	
	private boolean isRenderedBy(ViewGroup group)
	{
		if (group.indexOfChild(this) >= 0)
			return true;
		else
			return false;	 
	}
	
	private boolean hasItem(UIMenuButtonItem item) {
		if (this.indexOfChild(item.getView()) >= 0)
			return true;
		else
			return false;		
	}
}
