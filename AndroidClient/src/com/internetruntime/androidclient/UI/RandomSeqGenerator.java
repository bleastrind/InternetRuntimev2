package com.internetruntime.androidclient.UI;

import android.util.Log;

public  class RandomSeqGenerator
{
	private int IndexCount;
	private int avaliableIndexCount;
	private boolean flags[];
	
	public RandomSeqGenerator(int indexCount) {
		// TODO Auto-generated constructor stub
		this.IndexCount = indexCount;
		this.avaliableIndexCount = indexCount;
		flags = new boolean[indexCount];
		for (int i = 0; i < indexCount; i++)
			flags[i] = false;
	}
	
	public RandomSeq getRandomSeq()
	{
		RandomSeq seq =  new RandomSeq(IndexCount);
		for (int i = 0; i < IndexCount; i++)
			seq.set(i, getRandomIndexNoReplace());
		return seq;
	}
	
	public RandomSeq getRandomSeqStartWith(int start)
	{
		RandomSeq seq = new RandomSeq(IndexCount);
		seq.set(0, getSpecifiedIndexNoReplace(start));
		for (int i = 1; i < IndexCount; i++)
			seq.set(i, getRandomIndexNoReplace());
		return seq;
	}
	
	private int getSpecifiedIndexNoReplace(int index) {
		flags[index] = true;
		avaliableIndexCount--;
		return index;
	}
	
	private int getRandomIndexNoReplace() {
		int count = (int)Math.floor(Math.random() * (double)avaliableIndexCount);
		int ans = 0;
		while (true)
		{
			if (flags[ans])
			{
				ans++;
				continue;
			}
			if (count > 0)
			{
				ans++;
				count--;
			}
			else
				break;
		}
		flags[ans] = true;
		avaliableIndexCount--;
		Log.d("random", "random: " + String.valueOf(ans));
		return ans;
	}
}