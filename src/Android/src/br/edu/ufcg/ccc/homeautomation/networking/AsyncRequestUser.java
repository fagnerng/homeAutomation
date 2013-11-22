package br.edu.ufcg.ccc.homeautomation.networking;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncRequestUser extends AsyncTask<String, Void, User>{

	private RequestsCallback cb;
	private String user;
	private String pass;
	
	public AsyncRequestUser(RequestsCallback cb, String user, String pass) {
		this.cb = cb;
		this.user = user;
		this.pass = pass;
	}

	protected User doInBackground(String... params) {
		String jsonText = null;

		JSONObject jsonToSend = new JSONObject();
		try {
			jsonToSend.put("user", this.user);
			jsonToSend.put("pass", this.pass);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		String body = jsonToSend.toString();
		jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_TOKEN, body);
		
		System.out.println("RECEIVED JSON BODY WITH TOKEN: "+ jsonText);
		
		
		JSONObject json = null;
		try {
			json = new JSONObject(jsonText);
			String token = json.getString("token");
			
			JSONObject jsonToSend2 = new JSONObject();
			
			jsonToSend2.put("user", this.user);
			jsonToSend2.put("token", token);
			
			System.out.println("ENVIANDO: " + jsonToSend2.toString());
			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_USER, jsonToSend2.toString());
			
			System.out.println("RECEIVED JSON BODY WITH USER DATA: "+ jsonText);
			
			json = new JSONObject(jsonText);
			
			return new User(json, token);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;		
	}
	
	@Override
	protected void onPostExecute(User result) {
		super.onPostExecute(result);
		if (result != null)
			cb.onFinishRequestUser(result);
	}
	
	private JSONObject generateLoginBody(){
		//TODO
		return null;		
	}
	
	private JSONObject generateUserBody(){
		//TODO
		return null;
	}
}
