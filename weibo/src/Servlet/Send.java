package Servlet;

import java.io.IOException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Send implements Runnable{
	private String token;
	private String signalname;
	private Map<String,String> map;
	public Send(String token,String signalname,Map<String,String> map){
		this.token = token;
		this.signalname = signalname;
		this.map = map;
	}
	@Override
	public void run(){
		System.out.print("Send");
		try {
			config.properties.irt.send(token,signalname,map);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
