package br.edu.ufcg.ccc.homeautomation.networking;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

public class AsyncRequestSwitch extends AsyncTask<String, Void, Boolean>{
	
	private static final String REQUEST_FAILED = "err";
	private RequestsCallback cb;
	private Device mDevice;
	
	public AsyncRequestSwitch(RequestsCallback cb, Device device) {
		this.cb = cb;
		this.mDevice = device;
	}	

	@Override
	protected Boolean doInBackground(String... params) {
		String jsonText = null;

		jsonText = NetworkManager.requestPOST(RESTManager.URL_POST_DEVICE,
			mDevice.generateBody(UserManager.getInstance().generateBody()).toString());
		
		System.out.println("SWITCH RESPONSE: "+ jsonText);
		
		if (jsonText.equals(REQUEST_FAILED))
			return false;
		
		return true;		
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		cb.onFinishRequestSwitch(result);
	}

}
