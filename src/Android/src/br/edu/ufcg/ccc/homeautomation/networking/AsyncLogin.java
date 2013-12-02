package br.edu.ufcg.ccc.homeautomation.networking;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import br.edu.ufcg.ccc.homeautomation.AdminActivity;
import br.edu.ufcg.ccc.homeautomation.ChildActivity;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class AsyncLogin extends AsyncTask<Void, Void, String> {
	private Context mContext;
	private String user;
	private String pass;
	private ProgressDialog mProgressDialog;

	public AsyncLogin(Context context) {
		mContext = context;
	}

	@Override
	protected String doInBackground(Void... params) {

		String jsonText = NetworkManager.requestGET(RESTManager.URL_GET_USER
				+ generateBody());
		
		return jsonText;
	}

	private String generateBody() {
		String retorno = "?user=";

		retorno += this.user;
		retorno += "&pass=";
		retorno += this.pass;

		return retorno;

	}

	@Override
	protected void onPreExecute() {
		mProgressDialog = ProgressDialog.show(mContext, "", "", false, false);
		Activity mMA = (Activity) mContext;
		user = ((EditText) mMA.findViewById(R.id.username)).getText().toString();
		pass = ((EditText) mMA.findViewById(R.id.password)).getText()
				.toString();
		// ((EditText) mMA.findViewById(R.id.password)).setText("");
		mProgressDialog.setTitle(mMA.getString(R.string.action_wait));
		mProgressDialog.setMessage(mMA
				.getString(R.string.action_login_in_progress));

	}

	@Override
	protected void onPostExecute(String result) {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Erro:");
		AlertDialog alerta;
		builder.setNeutralButton("OK",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.cancel();

					}
				});
		try {
			JSONObject jsonToParse = new JSONObject(result);
			try {
				String err = jsonToParse.getString("err");
				if (err.equals("user-not-found")){
					builder.setMessage(mContext.getResources().getString(R.string.user_not_found));
				}else if  (err.equals("invalid-password")){
					builder.setMessage(mContext.getResources().getString(R.string.invalid_password));	
				} else if(err.equals("missing-parameters")){
					builder.setMessage(mContext.getResources().getString(R.string.missing_parameters));
				}else{
					builder.setMessage(mContext.getResources().getString(R.string.unkwon_erro));
				}
				alerta = builder.create();
				// Exibe
				alerta.show();
			} catch (JSONException e) {
				User mUser = new User(jsonToParse);
				Intent intent= null;
				if (mUser.isAdmin()){
					intent = new Intent(mContext, AdminActivity.class);
				}else{
					intent = new Intent(mContext, ChildActivity.class);
				}
				
				
				Bundle bundle = new Bundle();
				bundle.putSerializable("userJson",mUser);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			builder.setMessage(mContext.getResources().getString(R.string.server_is_down));
			alerta = builder.create();
			alerta.show();
		}

		mProgressDialog.cancel();
	}
}
