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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends Activity {

	public String urlPrefix = "http://www.posttestserver.com/data/2014/11/12/lirong/";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		new Thread() {
			@Override
			public void run() {
				get(urlPrefix);
				//postData("http://posttestserver.com/post.php");
			}
		}.start();
	}
	
	public void postData(String url) {
	    // Create a new HttpClient and Post Header
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httppost = new HttpPost(url);

	    try {
	        // Add your data
	        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
	        nameValuePairs.add(new BasicNameValuePair("title", "JoinPay"));
	        nameValuePairs.add(new BasicNameValuePair("name", "Lirong"));
	        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        // Execute HTTP Post Request
	        HttpResponse response = httpclient.execute(httppost);
	        String result = inputStreamToString(response.getEntity().getContent()).toString();
	        //String result = convertStreamToString(response.getEntity().getContent());
			Log.d("HttpPost result", result);
//			String[] items1 = result.split("View it at ");
//			String[] items2 = items1[1].split("Post body was");
//			Log.d("HttpPost result", items2[0]);
			
	        
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	    }
	} 

	public void get(String url)
	{
		HttpClient httpclient = new DefaultHttpClient();

		// Prepare a request object
		HttpGet httpget = new HttpGet(url); 

		// Execute the request
		HttpResponse response;
		try {
			response = httpclient.execute(httpget);
			// Examine the response status
			Log.i("Praeda",response.getStatusLine().toString());

			// Get hold of the response entity
			HttpEntity entity = response.getEntity();
			// If the response does not enclose an entity, there is no need
			// to worry about connection release

			if (entity != null) {

				// A Simple JSON Response Read
				InputStream instream = entity.getContent();
				String result= convertStreamToString(instream);

				Log.d("HttpGet result", result);
				
				String html = result;
				Document doc = Jsoup.parse(html);
				

				Elements link = doc.select("a");
				
				for (Element elm : link) {
					String text = elm.text();
					//Log.d("HttpPost text", elm.text());
					if (text.matches("[0-9][0-9].[0-9][0-9].[0-9]+")) {
						Log.d("HttpPost text", elm.text());
						response = httpclient.execute(new HttpGet(urlPrefix + text));
						result = convertStreamToString(response.getEntity().getContent());
						Log.d("HttpGet result" + text, result);
					}
				}
				
				
				
//				Element link = doc.select("a").first();
//
//				String text = doc.body().text(); // "An example link"
//				String linkHref = link.attr("href"); // "http://example.com/"
//				String linkText = link.text(); // "example""
//
//				String linkOuterH = link.outerHtml(); 
//				    // "<a href="http://example.com"><b>example</b></a>"
//				String linkInnerH = link.html(); // "<b>example</b>"
//				Log.d("HttpPost text", text);
//				Log.d("HttpPost linkHref", linkHref);
//				Log.d("HttpPost linkText", linkText);
//				Log.d("HttpPost linkOuterH", linkOuterH);
//				Log.d("HttpPost linkInnerH", linkInnerH);
				
				
				// now you have the string representation of the HTML request
				instream.close();
			}


		} catch (Exception e) {
			Log.e("HttpGet", e.toString());
		}
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
	
	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the BufferedReader.readLine()
		 * method. We iterate until the BufferedReader return null which means
		 * there's no more data to read. Each line will appended to a StringBuilder
		 * and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onButtonClick(View view){
		Log.d("button", "click");
		startActivity(new Intent(this, RadarViewActivity.class));
	}

	public void onStartServiceClick(View v){
		Log.d("Service", "start?");
		Intent i = new Intent(getBaseContext(), MessageRetrievalService.class);
		getBaseContext().startService(i);
	}
}
