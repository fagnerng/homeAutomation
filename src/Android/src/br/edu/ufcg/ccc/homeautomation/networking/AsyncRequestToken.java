package br.edu.ufcg.ccc.homeautomation.networking;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

public class AsyncRequestToken extends AsyncTask<String, Void, String>{

	private String user; 
	private String pass;
	
	public AsyncRequestToken(String pass) {
		this.pass = pass;
	}
	
	@Override
	protected void onPreExecute() {
		
		user = UserManager.getInstance().getUser();
	}

	protected String doInBackground(String... params) {
		String jsonText = null;
		jsonText = NetworkManager.requestPOST(RESTManager.URL_TOKEN, generateBody());
		
		return jsonText;
	}
	
	private String generateBody() {
		String jsonStr  ="{\"user\" : \"";
		jsonStr += user;
		jsonStr += "\", \"pass\" : \"";
		jsonStr += pass;
		jsonStr += "\"}";
		return jsonStr;
	}

	@Override
	protected void onPostExecute(String result) {
		if (result != null){
			String token = JsonParser.parseRefreshToken(result);
			if (token != null)
				UserManager.getInstance().setToken(token);
		}
	}
}
