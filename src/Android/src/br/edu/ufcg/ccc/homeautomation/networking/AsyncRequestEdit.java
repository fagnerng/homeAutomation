package br.edu.ufcg.ccc.homeautomation.networking;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncRequestEdit  extends AsyncTask<String, Void, Boolean>{

	private static final String REQUEST_FAILED = "err";
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

	protected Boolean doInBackground(String... params) {
		String jsonText = null;

		jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_TOKEN, generateBody(name, email, pass, lat, lon));
		if (jsonText.equals(REQUEST_FAILED))
			return false;
		
		return true;		
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result != null)
			cb.onFinishRequestEdit(result);
	}
	
	/**
	 * This method generates a JSONObject to be seended as the login request body
	 * @return String with the user name and the his token
	 */
	private String generateBody(String name, String email, String pass, double lat, double lon){
		
		JSONObject jsonToSend = new JSONObject();
		
		try {
			if (name != null)
				jsonToSend.put("name", this.name);
			if (email != null)
				jsonToSend.put("email", this.email);
			if (pass != null)
				jsonToSend.put("pass", this.pass);
			if (lat != -1)
				jsonToSend.put("pass", lat);
			if (lon != -1)
				jsonToSend.put("long", lon);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		return jsonToSend.toString();
	}
	
}
