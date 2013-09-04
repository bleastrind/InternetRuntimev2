package com.internetruntime.androidclient.Communication;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.codebutler.android_websockets.WebSocketClient;

public class SocketDriver {
	private Context context;
	WebSocketClient client = null; 
	private SocketHandler dataHandler;
	
	public SocketDriver(String username, String password, Context context) {
		this.context = context;
		dataHandler = new SocketHandler(context);
		List<BasicNameValuePair> extraHeaders = new ArrayList<BasicNameValuePair>();
		extraHeaders.add(new BasicNameValuePair("username", username));
		extraHeaders.add(new BasicNameValuePair("password", password));
		client = new WebSocketClient(URI.create(URIList.SOCKET_URI),
				new WebSocketClient.Listener() {
					
			
					@Override
					public void onMessage(byte[] data) {
						// TODO Auto-generated method stub
//						Log.d("comm", "get msg!");
						dataHandler.handle(data);
					}
					
					@Override
					public void onMessage(String data) {
						// TODO Auto-generated method stub
						Log.d("comm", "get msg!" + data);
						dataHandler.handle(data);
					}
					
					@Override
					public void onError(Exception arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onDisconnect(int arg0, String arg1) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onConnect() {
						// TODO Auto-generated method stub
						Log.d("comm", "Connected!");
					}
				}, null);
		
		
		
	}
	
	public void connect()
	{
		client.connect();
		Toast.makeText(context, String.valueOf(client.isConnected()), 3000).show();
	}
	
	public void disconnect()
	{
		client.disconnect();
	} 
	
	public void send(String msg)
	{
		client.send(msg);
	}
	
	public void send(byte[] data)
	{
		client.send(data);
	}
	
}
