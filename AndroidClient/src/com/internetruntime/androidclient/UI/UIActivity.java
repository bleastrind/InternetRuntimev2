package com.internetruntime.androidclient.UI;

import java.util.Stack;

import com.example.internetruntime.androidclient.R;

import android.app.Activity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UIActivity extends Activity{
	
	private ListMenu CurrentMenu;
	private Stack<ListMenu> BackMenuStack = new Stack<ListMenu>();

	public void start(ListMenu menu)
	{
		menu.start();
		setCurrentMenu(menu);
	}
	
	public ListMenu popBackMenuStack()
	{
		return BackMenuStack.pop();
	}
	
	public void setCurrentMenu(ListMenu CurrentMenu) {
		if (this.CurrentMenu != null)
			BackMenuStack.push(this.CurrentMenu);
		this.CurrentMenu = CurrentMenu;
	}
	
	
	public RelativeLayout getMainLayout() {
		return (RelativeLayout) findViewById(R.id.main);
	}
	
	@Override
	public void onBackPressed() {	
		if (BackMenuStack.empty())
		{			
			System.exit(0);
		}
		else
		{
			CurrentMenu.backToLast();
		}
	}
}
