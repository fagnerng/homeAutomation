package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncRequestChild  extends AsyncTask<String, Void, ArrayList<User>>{

	private RequestsCallback cb;
	private String name;
	private String email;
	private String pass;
	private double lat;
	private double lon;
	
	public AsyncRequestChild(RequestsCallback cb, String name, String email, String pass, double lat, double lon) {
		this.cb = cb;
		this.name = name;
		this.email = email;
		this.pass = pass;
		this.lat = lat;
		this.lon = lon;
	}

	protected ArrayList<User> doInBackground(String... params) {
		String jsonText = null;

		jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_TOKEN, generateBody(name, email, pass, lat, lon));
		
		return null;		
	}
	
	@Override
	protected void onPostExecute(ArrayList<User> result) {
		super.onPostExecute(result);
		if (result != null)
			cb.onFinishRequestChild(result);
	}
	
	/**
	 * This method generates a JSONObject to be seended as the login request body
	 * @return String with the user name and the his token
	 */
	private String generateBody( String name, String email, String pass, double lat, double lon){
		
		JSONObject jsonToSend = new JSONObject();
		try {
			
			jsonToSend.put("pass", this.pass);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		return jsonToSend.toString();
	}
	
}
