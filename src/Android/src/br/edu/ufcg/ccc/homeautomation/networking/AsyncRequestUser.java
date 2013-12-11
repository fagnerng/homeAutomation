package br.edu.ufcg.ccc.homeautomation.networking;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.Root;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

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
		
		JSONObject json = null;
		try {
			
			json = new JSONObject(jsonText);// Create a new JSONOBject to guard the received token from the server
			String token = json.getString("token");
			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_USER, generateUserBody(token));
			
			if (! jsonText.contains("err")){
				json = new JSONObject(jsonText);
				return new Root(json);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;		
	}
	
	@Override
	protected void onPostExecute(User result) {
		// Set for All System the userName and his current Token
		System.out.println("ALOW: "+result.getToken());
		UserManager.getInstance().setToken(result.getToken());
		
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
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonToSend.toString();
	}
	
	/**
	 * @param token - Uses the Received token from login
	 * @return String with the JSONBODY to request a user from server
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
