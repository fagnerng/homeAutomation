package br.edu.ufcg.ccc.homeautomation;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.service.NotificationService;

public class PrefsActivity extends PreferenceActivity{
	 
@SuppressWarnings("deprecation")
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.preference);
   

}
@Override
	protected void onStop() {
		super.onStop();
		boolean enabled = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("distance", true);
		Intent i = new Intent(this.getApplicationContext(), NotificationService.class);
		Toast.makeText(this,"enable"+enabled, Toast.LENGTH_SHORT).show();
		try{
			if (enabled){
				startService(i);
			}else{
				stopService(i);
			}
		}catch (Exception e1){
		}
	}

};
