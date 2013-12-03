package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

public class AsyncRequestChild  extends AsyncTask<String, Void, ArrayList<User>>{

	private RequestsCallback cb;
	private String userChild;
	
	public AsyncRequestChild(RequestsCallback cb, String userChild) {
		this.cb = cb;
		this.userChild = userChild;
	}

	protected ArrayList<User> doInBackground(String... params) {
		String jsonText = null;
		
		jsonText = NetworkManager.requestGET(RESTManager.URL_GET_CHILD + generateBody(userChild));
		System.out.println("CHILD RESPONSE: "+ jsonText);
		return JsonParser.parseAllChilds(jsonText);// Create a new JSONOBject to guard the received childs from the server
	}
	
	@Override
	protected void onPostExecute(ArrayList<User> result) {		
		if (result != null)
			cb.onFinishRequestChild(result);
	}
	
	/**
	 * This method generates a JSONObject to be seended as the login request body
	 * @return String with the user name and the his token
	 */
	private String generateBody(String userChild){
		String URLcomplement = "";
		
		URLcomplement += "?user=" + UserManager.getInstance().getUser() + "&" +
		"token=" + UserManager.getInstance().getToken();
			
		return URLcomplement;
	}
	
}
