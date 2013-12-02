package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncRequestChild  extends AsyncTask<String, Void, ArrayList<User>>{

	private RequestsCallback cb;
	private String userChild;
	
	public AsyncRequestChild(RequestsCallback cb, String userChild) {
		this.cb = cb;
		this.userChild = userChild;
	}

	protected ArrayList<User> doInBackground(String... params) {
		String jsonText = null;
//		ArrayList<User> childs = new ArrayList<User>();

		jsonText = NetworkManager.requestPOST(RESTManager.URL_GET_CHILD, generateBody(userChild));
		
		return JsonParser.parseAllChilds(jsonText);// Create a new JSONOBject to guard the received childs from the server
			
//		return childs;		
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
	}
	
}
