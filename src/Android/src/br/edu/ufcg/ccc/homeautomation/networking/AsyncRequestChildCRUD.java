package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncRequestChildCRUD  extends AsyncTask<String, Void, ArrayList<User>>{
	
	private HTTPType type;

	private RequestsCallback cb;
	private String userChild;
	private ArrayList<Integer> devices;
	private String name;
	private String email;
	private String pass;
	private String house;
	
	/**
	 * Constructor to execute a POST METHOD, to update a child
	 * @param cb - CallBack to receive the request result
	 * @param userChild - The login of the update child
	 * @param devs - The device's ArrayList of the child
	 */
	public AsyncRequestChildCRUD(RequestsCallback cb, String userChild, ArrayList<Integer> devs) {
		this.type = HTTPType.POST;
		this.cb = cb;
		this.userChild = userChild;
		this.devices = devs;
	}

	/**
	 * Constructor to execute a PUT METHOD, to create a child
	 * @param cb - CallBack to receive the request result
	 * @param userChild - The login of the update child
	 * @param devs - The ArrayList of devices
	 * @param name - String with the child name
	 * @param email - String with the child email
	 * @param pass - String with the child pass
	 * @param house - String with the child house
	 */
	public AsyncRequestChildCRUD(RequestsCallback cb, String userChild, ArrayList<Integer> devs, String name,
			String email, String pass, String house ) {
		this.type = HTTPType.PUT;
		this.cb = cb;
		this.userChild = userChild;
		this.devices = devs;
		this.name = name;
		this.email = email;
		this.pass = pass;
		this.house = house;
	}
	
	/**
	 * Constructor to execute a DELETE METHOD, to create a child
	 * @param cb - CallBack to receive the request result
	 * @param userChild - The login of the update child
	 */
	public AsyncRequestChildCRUD(RequestsCallback cb, String userChild) {
		this.type = HTTPType.DELETE;
		this.cb = cb;
		this.userChild = userChild;
	}	

	protected ArrayList<User> doInBackground(String... params) {
		String jsonText = null;		

		// TROCAR PRA CASE
		if (type == HTTPType.POST){
//			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_CHILD, generateBody(userChild));
		}else if(type == HTTPType.PUT){
//			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_CHILD, generateBody(userChild));			
		}else if(type == HTTPType.DELETE){
//			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_CHILD, generateBody(userChild));
		}
		
		return JsonParser.parseAllChilds(jsonText);// Create a new JSONOBject to guard the received childs from the server
	}
	
	@Override
	protected void onPostExecute(ArrayList<User> result) {
		super.onPostExecute(result);
		if (result != null)
			cb.onFinishRequestChild(result);
	}
	
/*	
	*//**
	 * This method generates a JSONObject to be seended as the login request body
	 * @return String with the user name and the his token
	 *//*
	private String generateBody(String userChild){
		
		JSONObject jsonToSend = new JSONObject();
		
		try {
			jsonToSend.put("user", User.user);
			jsonToSend.put("token", User.currentToken);
			
			if (userChild != null)
				jsonToSend.put("child", userChild);
			
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		return jsonToSend.toString();
	}*/
	
}
