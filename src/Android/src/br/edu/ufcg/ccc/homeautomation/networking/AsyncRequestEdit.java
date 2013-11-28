package br.edu.ufcg.ccc.homeautomation.networking;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncRequestEdit  extends AsyncTask<String, Void, User>{

	private RequestsCallback cb;
	private String name;
	private String email;
	private String pass;
	private double lat;
	private double lon;
	
	public AsyncRequestEdit(RequestsCallback cb, String name, String email, String pass, double lat, double lon) {
		this.cb = cb;
		this.name = name;
		this.email = email;
		this.pass = pass;
		this.lat = lat;
		this.lon = lon;
	}

	protected User doInBackground(String... params) {
		String jsonText = null;

		jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_TOKEN, generateBody(name, email, pass, lat, lon));
		
		return null;		
	}
	
	@Override
	protected void onPostExecute(User result) {
		super.onPostExecute(result);
		if (result != null)
			cb.onFinishRequestUser(result);
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
