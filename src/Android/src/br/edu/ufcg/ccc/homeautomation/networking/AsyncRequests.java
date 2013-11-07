package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Application;
import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncRequests extends AsyncTask<String, Void, ArrayList<User>>{
	
	private static final String URL_POPULAR_APPS = "GetAppList";
	private static final String URL_POP_RANGE_APPS = "GetAppListRange";
	private static final String URL_AOC_APPS = "GetAocExclusiveApps";
	private static final String URL_TOP_APPS = "GetTopPartnersApps";

	private RequestsCallback cb;
	
	public AsyncRequests(RequestsCallback cb, int from, int quantity) {
		this.cb = cb;
	}

	protected ArrayList<User> doInBackground(String... params) {
		String url = RESTManager.URL_GET_ALL_USERS;
			
		String jsonText = null;
		
		JSONObject json = new JSONObject();
		try {
			json.put("Density", "valor Densidade");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		String body = json.toString();
		jsonText = NetworkManager.requestPOST(url+URL_POPULAR_APPS, body);
		return JsonParser.parseAllUsers(jsonText);
	}
	
	@Override
	protected void onPostExecute(ArrayList<User> result) {
		super.onPostExecute(result);
		if (result != null)
			cb.onFinish(result);
	}
}
