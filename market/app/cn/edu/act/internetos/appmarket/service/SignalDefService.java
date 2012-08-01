package cn.edu.act.internetos.appmarket.service;

import java.util.List;

import models.SignalDefDao;

public class SignalDefService {
	public List<String> getSignalDefs(){
		return new SignalDefDao().getAllSignals();
	}
}
