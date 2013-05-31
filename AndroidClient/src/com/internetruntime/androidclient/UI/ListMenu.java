package com.internetruntime.androidclient.UI;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

public class ListMenu extends RelativeLayout
{
	private int id;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;
	
	private Context context;
	private List<MenuItem> items = new ArrayList<MenuItem>();
	
	
	public ListMenu(Context context)
	{
		super(context);
		this.context = context;
		
		id = UIConst.ID_COUNT++;
		setId(id);
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getSize()
	{
		return items.size();
	}
	
	public void addItem(MenuItem item)
	{
		items.add(item);
		RelativeLayout.LayoutParams params = new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		if (items.size() > 1)
		{
			params.addRule(RelativeLayout.BELOW, items.get(items.size() - 2).getId());
		}
		item.setLayoutParams(params);
		addView(item);
		item.setParentMenu(this);
		
	}
	
	public void goToMenu(ListMenu newMenu, int startIndex)
	{
		UIActivity activity = (UIActivity)context;
		int n = Math.max(getSize(), newMenu.getSize());
		RandomSeqGenerator generator = new RandomSeqGenerator(n);
		RandomSeq seq = generator.getRandomSeqStartWith(startIndex);
		newMenu.showFlyLeft(UIConst.LIST_MENU_CHANGE_INTERVAL, seq);
		activity.setCurrentMenu(newMenu);
		hideFlyRight(0, seq);
	}
	
	public void backToLast()
	{
		UIActivity activity = (UIActivity)context;
		ListMenu newMenu = activity.popBackMenuStack();
		int n = Math.max(getSize(), newMenu.getSize());
		RandomSeqGenerator generator = new RandomSeqGenerator(n);
		RandomSeq seq = generator.getRandomSeq();
		newMenu.showFlyLeft(UIConst.LIST_MENU_CHANGE_INTERVAL, seq);
		hideFlyRight(0, seq);
	}
	
	
	public void start()
	{
		show(0, null, LEFT);
	}
	
	//show fly left
	
	public void showFlyLeft(int delay)
	{
		show(delay, null, LEFT);
	}
	
	public void showFlyLeft(int delay, int first)
	{
		RandomSeqGenerator generator = new RandomSeqGenerator(items.size());
		RandomSeq seq = generator.getRandomSeqStartWith(first);
		show(delay, seq, LEFT);
	}
	
	public void showFlyLeft(int delay, RandomSeq seq)
	{
		show(delay, seq, LEFT);
	}
	
	//hide fly right
	
	public void hideFlyRight(int delay)
	{
		hide(delay, null, RIGHT);
	}
	
	public void hideFlyRight(int delay, int first)
	{
		RandomSeqGenerator generator = new RandomSeqGenerator(items.size());
		RandomSeq seq = generator.getRandomSeqStartWith(first);
		hide(delay, seq, RIGHT);
	}
	
	public void hideFlyRight(int delay, RandomSeq seq)
	{
		hide(delay, seq, RIGHT);
	}
	
	
	
	private void show(int pubDelay, RandomSeq seq, int orient)
	{
		int n = items.size();
		if (seq == null)
		{
			RandomSeqGenerator generator = new RandomSeqGenerator(n);
			seq = generator.getRandomSeq();
		}
		for(int i = 0; i < n; i++)
		{
			int delay = pubDelay + seq.indexOf(i) * UIConst.BTN_FADE_INTERVAL;
			Log.d("ui", String.valueOf(delay));
			if (orient == LEFT)
				items.get(i).flyInLeft(delay);
			else
				items.get(i).flyInRight(delay);
		}
	}
	
	private void hide(int pubDelay, RandomSeq seq, int orient)
	{
		int n = items.size();
		if (seq == null)
		{
			RandomSeqGenerator generator = new RandomSeqGenerator(n);
			seq = generator.getRandomSeq();
		}
		for(int i = 0; i < n; i++)
		{
			int delay = pubDelay + seq.indexOf(i) * UIConst.BTN_FADE_INTERVAL;
			if (orient == LEFT)
				items.get(i).flyOutLeft(delay);
			else
				items.get(i).flyOutRight(delay);
		}
	}
	

	
	
	
	
	
	
	
	
}
