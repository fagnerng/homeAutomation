package br.edu.ufcg.ccc.homeautomation.networking;

import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.service.NotificationService;

public class AsyncResquestStatusDevs extends AsyncTask<Void, Void, String>{


	@Override
	protected String doInBackground(Void... params) {
		
			
			try {
				return NetworkManager.requestGET(RESTManager.URL_DEVICE+generateBody());
			} catch (Exception e) {
				return  null;
			}
		
	}


	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		
	}
	private String generateBody(){
		String retorno = "?user=";
		retorno += UserManager.getInstance().getUser();
		retorno += "&token=";
		retorno += UserManager.getInstance().getToken();
		return retorno;
	}
	
	@Override
	protected void onPostExecute(String result) {
		NotificationService.onEndResquest(result);
	
	}

	
}
