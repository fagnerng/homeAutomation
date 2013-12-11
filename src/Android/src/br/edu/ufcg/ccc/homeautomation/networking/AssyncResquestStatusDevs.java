package br.edu.ufcg.ccc.homeautomation.networking;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.DefaultHttpClient;

import br.edu.ufcg.ccc.homeautomation.service.NotificationService;

import android.os.AsyncTask;

public class AssyncResquestStatusDevs extends AsyncTask<Void, Void, String>{
		int timeToSleep;

	@Override
	protected String doInBackground(Void... params) {
		
			
			try {
				Thread.sleep(timeToSleep);
				return request();
			} catch (Exception e) {
				return  null;
			}
		
	}

	@Override
	protected void onPreExecute() {
		timeToSleep = 10* 1000;
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		
	}
	private String request(){
		String requestUrl = "http://192.168.2.28:9000/adevice?user=fagnerng&token=abc";
		String result = null;
		InputStream content = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet(requestUrl));
			
			content = response.getEntity().getContent();
			result = convertStreamToString(content);
		} catch (HttpHostConnectException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
		
	@Override
	protected void onPostExecute(String result) {
		NotificationService.onEndResquest(result);
	
	}

	 public static String convertStreamToString(java.io.InputStream is) {
	    	java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
	    	return s.hasNext() ? s.next() : "";
	    }
}
