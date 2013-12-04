package br.edu.ufcg.ccc.homeautomation.networking;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

public class AsyncRequestEdit extends AsyncTask<String, Void, Boolean>{

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

		jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_USER, generateBody(name, email, pass, lat, lon));
		System.out.println("EDIT RESPONSE: "+ jsonText);
		
		if (jsonText.equals(REQUEST_FAILED))
			return false;
		
		return true;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		cb.onFinishRequestEdit(result);
	}
	
	/**
	 * This method generates a JSONObject to be passed as the login request body
	 * @return String with the user name and the his token
	 */
	private String generateBody(String name, String email, String pass, double lat, double lon){
		
		JSONObject jsonToSend = new JSONObject();
		
		try {
			jsonToSend.put("user", UserManager.getInstance().getUser());
			jsonToSend.put("token", UserManager.getInstance().getToken());
			
			if (name != null)
				jsonToSend.put("name", name);
			if (email != null)
				jsonToSend.put("email", email);
			if (pass != null)
				jsonToSend.put("pass", pass);
			if (lat != -1)
				jsonToSend.put("lati", lat);
			if (lon != -1)
				jsonToSend.put("long", lon);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		return jsonToSend.toString();
	}
	
}
