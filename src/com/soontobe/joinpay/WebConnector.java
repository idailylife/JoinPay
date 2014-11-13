package com.soontobe.joinpay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;

public class WebConnector {

	private String userName;

	public WebConnector(String userName) {
		this.userName = userName;
	}

	public void onlineSignIn(String url) {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("status", Constants.userName + "IsOnline"));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);
			String result = inputStreamToString(response.getEntity().getContent()).toString();
			//String result = convertStreamToString(response.getEntity().getContent());
			Log.d("HttpPost result", result);
			//				String[] items1 = result.split("View it at ");
			//				String[] items2 = items1[1].split("Post body was");
			//				Log.d("HttpPost result", items2[0]);


		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	} 

	public String getFile(String url)
	{
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(url); 

		ArrayList<String> ret = new ArrayList<String>();
		// Execute the request
		HttpResponse response;
		String result = "";
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				result = inputStreamToString(instream).toString();
			}
		} catch (Exception e) {
			Log.e("HttpGet", e.toString());
		}

		return result;
	}

	public ArrayList<String> getFileNameList(String url, int visitedFilesCount)
	{
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(url); 

		ArrayList<String> ret = new ArrayList<String>();
		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result= inputStreamToString(instream).toString();

				//				Log.d("HttpGet result", result);

				String html = result;
				Document doc = Jsoup.parse(html);

				Elements link = doc.select("a");

				int cnt = 0;
				for (Element elm : link) {
					String text = elm.text();
					//Log.d("HttpPost text", elm.text());
					if (text.matches("[0-9][0-9].[0-9][0-9].[0-9]+")) {
						if (cnt >= visitedFilesCount) {
							ret.add(text);
						}
						cnt++;
					}
				}
				instream.close();
			}
		} catch (Exception e) {
			Log.e("HttpGet", e.toString());
		}
		Log.d("getFileNameList", "files num: " + ret.size());
		return ret;
	}

	private StringBuilder inputStreamToString(InputStream is) {
		String line = "";
		StringBuilder total = new StringBuilder();

		// Wrap a BufferedReader around the InputStream
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		// Read response until the end
		try {
			while ((line = rd.readLine()) != null) { 
				total.append(line); 
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Return full string
		return total;
	}		
}
