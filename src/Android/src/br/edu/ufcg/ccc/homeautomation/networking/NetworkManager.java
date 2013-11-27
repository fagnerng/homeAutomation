package br.edu.ufcg.ccc.homeautomation.networking;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import br.edu.ufcg.ccc.homeautomation.exceptions.NoConnectionExceptionListener;
import br.edu.ufcg.ccc.homeautomation.exceptions.RequestUnauthorizedExeption;

/**
 * NetworkManager class is responsible for creating an interface between the
 * RESTManager and the network
 */
public class NetworkManager {
	
	private static final int SUCCESS_CODE_MIN = 200;
	private static final int SUCCESS_CODE_MAX = 299;
	
	public static Resources getOutsideRes;
	private static NoConnectionExceptionListener noConnectionListener;
	private static RequestUnauthorizedExeption requestUnauthorized;
	
	public static boolean canDownload = true;
	
	public static void setNoConnectionListener(NoConnectionExceptionListener listener) {
		noConnectionListener = listener;
	}
	
	public static void setUnauthorized(RequestUnauthorizedExeption listener) {
		requestUnauthorized = listener;
	}
	
	/**
	 * Mades a GET request from a received url 
	 * @param url - Represents the Link that the request will made for
	 * @return String with the received data from the GET request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String requestPOST(String requestUrl) {
		String result = null;
		InputStream content = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpPost(requestUrl));
			if (!isValidHttpCode(response.getStatusLine().getStatusCode()) && noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
			
			content = response.getEntity().getContent();
			result = convertStreamToString(content);
		} catch (HttpHostConnectException e) {
			if (noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String requestPOST(String url, String body){
		
		HttpClient hc = new DefaultHttpClient();
		HttpPost p = new HttpPost(url);
		StringEntity se = null;
		String deviceID = "";
		
		try{
			p.setEntity(new StringEntity(HTTP.UTF_8));
			se = new StringEntity(body, HTTP.UTF_8);
		}catch(Exception e){
			e.printStackTrace();
		}
		p.addHeader("User-Agent-Model", deviceID);
		p.setHeader("Content-type", "application/json; charset=utf-8");
		p.setEntity(se);
		String response = "";
		
		try{
			HttpResponse resp = hc.execute(p);
			if (!isValidHttpCode(resp.getStatusLine().getStatusCode()) && noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
			
			HttpEntity entity = resp.getEntity();
			if(entity != null) {
				InputStream is = entity.getContent();
				response = convertStreamToString(is);	
			}

		} catch (HttpHostConnectException e) { // No Connection
			if (noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
		} catch (UnknownHostException e) {
			if (noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
		} catch (Exception e){
			e.printStackTrace();
		}
		return response;
	}
	
	/**
	 * Mades a GET request from a received url 
	 * @param url - Represents the Link that the request will made for
	 * @return String with the received data from the GET request
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String requestGET(String requestUrl) {
		String result = null;
		InputStream content = null;
		HttpClient httpclient = new DefaultHttpClient();
		HttpResponse response;
		try {
			response = httpclient.execute(new HttpGet(requestUrl));
			if (!isValidHttpCode(response.getStatusLine().getStatusCode()) && noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
			
			content = response.getEntity().getContent();
			result = convertStreamToString(content);
		} catch (HttpHostConnectException e) {
			if (noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

    /**
     * Parser that converts Stream to String
     * @param is - Represents the InpuntStream that will be converted in String
     * @return	String converted
     */
    public static String convertStreamToString(java.io.InputStream is) {
    	java.util.Scanner s = new java.util.Scanner(is, "UTF-8").useDelimiter("\\A");
    	return s.hasNext() ? s.next() : "";
    }
    
    private static boolean isValidHttpCode(int statusCode) {
    	if (statusCode >= SUCCESS_CODE_MIN && statusCode < SUCCESS_CODE_MAX)
    		return true;
    	return false;
    }
	
}
