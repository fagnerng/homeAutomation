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
import br.edu.ufcg.ccc.homeautomation.MainActivity;
import br.edu.ufcg.ccc.homeautomation.R;

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
		// System.out.println("RECEIVED JSON BODY WITH TOKEN: " + jsonText);

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
		user = ((EditText) mMA.findViewById(R.id.email)).getText().toString();
		pass = ((EditText) mMA.findViewById(R.id.password)).getText()
				.toString();
		// ((EditText) mMA.findViewById(R.id.password)).setText("");
		mProgressDialog.setTitle(mMA.getString(R.string.action_aguarde));
		mProgressDialog.setMessage(mMA
				.getString(R.string.action_login_in_progress));

	}

	@Override
	protected void onPostExecute(String result) {
		try {
			JSONObject jsonToParse = new JSONObject(result);
			try {
				String err = jsonToParse.getString("err");
				AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
				builder.setTitle("Erro:");
				builder.setMessage(err);
				builder.setNeutralButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.cancel();

							}
						});
				AlertDialog alerta = builder.create();
				// Exibe
				alerta.show();
			} catch (Exception e) {
				Intent intent = new Intent(mContext, MainActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("userJson",result);
				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mProgressDialog.cancel();
	}
}
