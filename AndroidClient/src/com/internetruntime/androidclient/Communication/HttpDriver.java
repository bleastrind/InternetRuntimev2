package com.internetruntime.androidclient.Communication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import android.view.InputEvent;

public class HttpDriver {
	private String urlString;
	private HttpURLConnection connection;
	
	
	public HttpDriver(String urlString) {
		this.urlString = urlString;
	}
	
	public void postFile(String token, File file)
	{
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(urlString);
		
		MultipartEntity multipartEntity = new MultipartEntity();
		ContentBody contentBody = new FileBody(file);
		multipartEntity.addPart(token, contentBody);
		httpPost.setEntity(multipartEntity);
//		httpPost.addHeader("token", token);
		
		try {
			Log.d("comm", "before post");
			HttpResponse response = httpClient.execute(httpPost);
			
			Log.d("comm", "pass post");
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
