package br.edu.ufcg.ccc.homeautomation;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.core.lbs.LBSManager;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.NetworkManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LocationActivity extends Activity {
	public static Context mContext;
	public static  EditText et_lati ;
	public static  EditText et_long;
	public static Button bt_save;
	public LBSManager mLbs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		mContext = this;
		mLbs = new LBSManager(mContext);
		mLbs.onStart();
		
		et_lati = (EditText) findViewById(R.id.et_latitude);
		et_long = (EditText) findViewById(R.id.et_longitude);
		bt_save = (Button) findViewById(R.id.bt_save_location);
		bt_save.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				JSONObject json = UserManager.getInstance().generateBody();
				try {
					json.put("lati", et_lati.getText());
					json.put("long", et_long.getText());
				} catch (JSONException e) {
					//  Auto-generated catch block
					
				}
				new AsyncTask<String, Void, Void>() {

					@Override
					protected Void doInBackground(String... params) {
						NetworkManager.requestPOST(RESTManager.URL_USER,
								params[0].toString());
						return null;
					}
				}.execute(json.toString());

				finish();

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}

	@Override
	protected void onStop() {
		super.onStop();
		mLbs.onStop();

	}
}
