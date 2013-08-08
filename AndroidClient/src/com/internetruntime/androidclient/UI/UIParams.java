package com.internetruntime.androidclient.UI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIParams {
	private Map<String, Integer> intParams = null;
	private Map<String, Boolean> boolParams = null;
	private Map<String, RandomSeq> seqParams = null;
	
	public void addInt(String name, int value) {
		if (intParams == null)
			intParams = new HashMap<String, Integer>();
		intParams.put(name, value);
	}
	
	public void addBool(String name, boolean value) {
		if (boolParams == null)
			boolParams = new HashMap<String, Boolean>();
		boolParams.put(name, value);
	}
	
	public void addUIRandomSeq(String name, RandomSeq value) {
		if (seqParams == null)
			seqParams = new HashMap<String, RandomSeq>();
		seqParams.put(name, value);
	}
	
	public int getInt(String name) {
		return intParams.get(name);
	}
	
	public boolean getBool(String name) {
		return boolParams.get(name);
	}
	
	public RandomSeq getUIRandomSeq(String name){
		return seqParams.get(name);
	}
	
}

