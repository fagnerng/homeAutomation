package br.edu.ufcg.ccc.homeautomation.networking;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Context;
import android.os.AsyncTask;

public class AssyncVerifyStatusTask extends AsyncTask<Void, Void, Void>{
	
	String mResult;
	Context mContext;
	public AssyncVerifyStatusTask(Context context, String arg0){
		this.mResult = arg0;
		this.mContext = context;
	}
	
	
	@Override
	protected Void doInBackground(Void... params) {
		JSONArray retorno = new JSONArray();
		if (mResult != null) {
			try {
				retorno = new JSONArray(mResult);
			} catch (JSONException e) {

			}
		}
		for (int i = 0; i < retorno.length(); i++) {
			try {
				System.out.println(retorno.getJSONObject(i));
				if (retorno.getJSONObject(i).getBoolean("status")) {
					new AssycMakeNotification(mContext,i,retorno.getJSONObject(i).getString("name")).execute();
					break;
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}

}
