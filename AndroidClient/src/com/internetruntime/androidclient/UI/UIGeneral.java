package com.internetruntime.androidclient.UI;

import android.view.View;
import android.view.ViewGroup;

public interface UIGeneral {
	public boolean render(ViewGroup group);	//render on another view group
//	public boolean remove();	//remove from parent view
	public boolean show(RandomSeq seq, int pubDelay, boolean backflag);	//show
	public boolean hide(RandomSeq seq, int pubDelay, boolean backflag);	//hide
	
}
