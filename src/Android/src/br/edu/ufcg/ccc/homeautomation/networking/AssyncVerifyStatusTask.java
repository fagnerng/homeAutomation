package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import br.edu.ufcg.ccc.homeautomation.entities.AirCondition;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.entities.Light;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

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
		User mUser = UserManager.getInstance().getUserObject();
		ArrayList<Device> mDev = new ArrayList<Device>();
		for (int i = 0; i < retorno.length(); i++) {
			try {
				Device tempDev;
				if( retorno.getJSONObject(i).getString("type").equals("light")){
					tempDev = new Light(retorno.getJSONObject(i));
				}else{
					tempDev = new AirCondition(retorno.getJSONObject(i));
				}
				mDev.add(tempDev);
				if (tempDev.getStatus()) {
					new AssycMakeNotification(mContext,i,tempDev.getName()).execute();
				
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
		mUser.setDevices(mDev);
		UserManager.getInstance().setUserObject(mUser);
		return null;
	}

}
