package com.internetruntime.androidclient.UI;

public class RandomSeq
{
	int data[];
	int count;
	public RandomSeq(int count) {
		// TODO Auto-generated constructor stub
		this.count = count;
		this.data = new int[count];
	}
	
	public void set(int index, int number) {
		data[index] = number;
	}
	
	public int get(int index) {
		if (index >= count)
			return index;
		else
			return data[index];
	}
	
	public int indexOf(int number)
	{
		if (number >= count)
			return number;
		else
			return getPos(number);
	}
	
	private int getPos(int number)
	{
		int pos = 0;
		while (data[pos] != number)
			pos++;
		return pos;
	}	
}