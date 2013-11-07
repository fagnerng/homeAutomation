package br.edu.ufcg.ccc.homeautomation.networking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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
		} catch (HttpHostConnectException e) { // No Connection
			if (noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
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
		} catch (HttpHostConnectException e) { // No Connection
			if (noConnectionListener != null)
				noConnectionListener.onNoConnectionException();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static String requestPOST(String url, String body){
		
		String pathStoreActivity = "/data/data/com.tpvtech.aocstore/files/INSTALLATION";
		
		HttpClient hc = new DefaultHttpClient();
		HttpPost p = new HttpPost(url);
		StringEntity se = null;
		String deviceID = "";
		
		try{
			FileReader archive = new FileReader(pathStoreActivity);
			BufferedReader readArchive = new BufferedReader(archive);
			deviceID = readArchive.readLine();
			archive.close();
			
			p.setEntity(new StringEntity(HTTP.UTF_8));
			se = new StringEntity(body,HTTP.UTF_8);
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
	 * Executes a GET request to the received token to download an Image
	 * @param token
	 * @return Bitmap Image
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static Drawable requestGetImage(String uri) throws ClientProtocolException, IOException{
		URL url = new URL(uri);
		Bitmap bitmap = null;

		URLConnection connection = null;
		
//		HttpURLConnection http = (HttpURLConnection) connection;
//		http.disconnect();
		try{	
			connection = url.openConnection();
			connection.setUseCaches(true);
		}catch(IOException e){
			
			//Change this comparison to a thing more constant (would be better)
			if (e.getMessage().equals("Received authentication challenge is null")){
				requestUnauthorized.onRequestUnauthorizedException();
				canDownload = false;
			}
//			}else
//				noConnectionListener.onNoConnectionException();
			
			return null;
		}
		
		Bitmap response = BitmapFactory.decodeStream((InputStream)connection.getContent());
		if (response instanceof Bitmap) {
			bitmap = (Bitmap)response;
		}
		Drawable imgDraw = new BitmapDrawable(getOutsideRes,bitmap);
		canDownload = true;
		return imgDraw;
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
    
    public static void downloadFile(
		final Context context,
		final String fileName,
		final String url,
		final OnDownloadProgressListener progressListener,
		final boolean forceDownload) {
    	
    	Runnable runnable = new Runnable() {
    		File outFile;
			@Override
			public void run() {
				
				InputStream stream = null;
	            FileOutputStream out = null;
				
	            try {
		            String path = Environment.getExternalStorageDirectory() + "/downloadAPKs/";
		            File file = new File(path);
		            file.mkdirs();
		            outFile = new File(file, fileName);
		            if (outFile.exists() && !true /*forceDownload*/) {
						progressListener.onProgress(100, outFile);
						return;
					}
		            
			    	URLConnection cn = new URL(url).openConnection();
			    	
			        cn.connect();
			        
			        stream = cn.getInputStream();
		            out = new FileOutputStream(outFile);
			
			        int totalByteRead = 0;
			        do {
			        	byte buf[];
			        	if (cn.getContentLength() - totalByteRead > 1024)
			        		buf = new byte[1024];
			        	else
			        		buf = new byte[cn.getContentLength() - totalByteRead];
			        	
			            int numread = stream.read(buf);
			            if (numread == -1)
			            	break;
			            totalByteRead += numread;
			            out.write(buf, 0, numread);
			            
			            progressListener.onProgress((int) ((
			            		(float) totalByteRead / (float) cn.getContentLength()) * 100), null);
			        } while (true);
			        
			        out.close();
			        stream.close();
			        progressListener.onProgress(100, outFile);
			        
				} catch (SocketTimeoutException e){
					e.printStackTrace();
					noConnectionListener.onNoConnectionException();
					outFile.delete();
					progressListener.onProgress(-1, null); // -1 means that the download failed
					
				} catch (IOException e) {
					e.printStackTrace();
					
					//Change this comparison to a thing more constant (would be better)
					if (e.getMessage().equals("Received authentication challenge is null"))
						requestUnauthorized.onRequestUnauthorizedException();
//					else
//						noConnectionListener.onNoConnectionException();
					 
					outFile.delete();
					progressListener.onProgress(-1, null); // -1 means that the download failed
					
				} finally {
					try {
					if (out != null)
						out.close();
					if (stream != null)
						stream.close();
					} catch (IOException e) {
					}
				}
			}
    	};
    	
    	Thread t = new Thread(runnable);
    	t.start();
    }
    
    private static boolean isValidHttpCode(int statusCode) {
    	if (statusCode >= SUCCESS_CODE_MIN && statusCode < SUCCESS_CODE_MAX)
    		return true;
    	return false;
    }
	
}
