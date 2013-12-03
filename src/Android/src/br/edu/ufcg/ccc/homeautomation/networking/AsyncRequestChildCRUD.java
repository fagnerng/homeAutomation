package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

public class AsyncRequestChildCRUD  extends AsyncTask<String, Void, Boolean>{
	
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

	protected Boolean doInBackground(String... params) {
		
		Boolean requestResult = false;
		String jsonText = null;

		if (type == HTTPType.POST){
			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_CHILD, generatePOSTBody(userChild, devices));
		}else if(type == HTTPType.PUT){
			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_CHILD, generatePUTBody(userChild, devices, name, email, pass, house));			
		}else if(type == HTTPType.DELETE){
			jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_CHILD, generateDELETEBody(userChild));
		}
		
		if (! jsonText.contains("err"))
			requestResult = true;
		
		return requestResult;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (result != null)
			cb.onFinishRequestChildCRUD(result);
	}
	
	
	/**
	 * This method generates a JSONObject to be seended as the login request body
	 * @return String with the user name and the his token
	 */
	private String generatePOSTBody(String userChild, ArrayList<Integer> devs){
		
		JSONObject jsonToSend = new JSONObject();
		JSONArray array = new JSONArray();
		
		for (int i = 0; i < devs.size(); i++) 
	        array.put(devs.get(i));
		
		try {
			jsonToSend.put("user", UserManager.getInstance().getUser());
			jsonToSend.put("token", UserManager.getInstance().getToken());
			jsonToSend.put("child", this.userChild);
			jsonToSend.put("devices", array);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonToSend.toString();
	}
	
	private String generatePUTBody(String userChild, ArrayList<Integer> devs,
						String name, String email, String pass, String house) {
		
		JSONObject jsonToSend = new JSONObject();
		JSONArray array = new JSONArray();
		
		for (int i = 0; i < devs.size(); i++) 
	        array.put(devs.get(i));
		
		try {
			jsonToSend.put("user", UserManager.getInstance().getUser());
			jsonToSend.put("token", UserManager.getInstance().getToken());
			jsonToSend.put("child", this.userChild);
			jsonToSend.put("devices", array);
			jsonToSend.put("name", name);
			jsonToSend.put("email", email);
			jsonToSend.put("pass", pass);
			jsonToSend.put("house", house);			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return jsonToSend.toString();		
	}
	
	private String generateDELETEBody(String userChild){
		JSONObject jsonToSend = new JSONObject();
		
		try{
			jsonToSend.put("user", UserManager.getInstance().getUser());
			jsonToSend.put("token", UserManager.getInstance().getToken());;
			jsonToSend.put("child", userChild);
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return jsonToSend.toString();
	}
	
}
