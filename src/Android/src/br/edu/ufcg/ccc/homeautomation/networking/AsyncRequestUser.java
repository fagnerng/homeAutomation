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

		jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_TOKEN, generateLoginBody());
		System.out.println("RECEIVED JSON BODY WITH TOKEN: "+ jsonText);
		
		JSONObject json = null;
		try {
			
			json = new JSONObject(jsonText); // Create a new JSONOBject to guard the received token from the server
			String token = json.getString("token");
			
			System.out.println("ENVIANDO: " + generateUserBody(token).toString());
			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_USER, generateUserBody(token));
			
			System.out.println("RECEIVED JSON BODY WITH USER DATA: "+ jsonText);
			if (jsonText.contains("err"))
				return null;
			
			json = new JSONObject(jsonText);
			return new User(json, token);
			
		} catch (JSONException e) {
			//e.printStackTrace();
		}
		
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
	private String generateLoginBody(){
		
		JSONObject jsonToSend = new JSONObject();
		try {
			jsonToSend.put("user", this.user);
			jsonToSend.put("pass", this.pass);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		return jsonToSend.toString();
	}
	
	/**
	 * @param token - Uses the Received token from login
	 * @return
	 */
	private String generateUserBody(String token){
		
		JSONObject jsonToGetUser = new JSONObject();
		try{
			jsonToGetUser.put("user", this.user);
			jsonToGetUser.put("token", token);
		}catch (JSONException e){
			e.printStackTrace();
		}
		return jsonToGetUser.toString();
	}
}
