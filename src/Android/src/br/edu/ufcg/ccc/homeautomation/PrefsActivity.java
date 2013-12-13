package br.edu.ufcg.ccc.homeautomation;

import br.edu.ufcg.ccc.homeautomation.service.NotificationService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.View;

public class PrefsActivity extends PreferenceActivity{
	 
@SuppressWarnings("deprecation")
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.preference);
   findViewById(R.xml.preference).setOnClickListener(new View.OnClickListener() {
	   SharedPreferences pref;
	
	@Override
	public void onClick(View v) {
		pref = PreferenceManager.getDefaultSharedPreferences(v.getContext());
		boolean enable =pref.getBoolean("distance", true);
		Intent i = new Intent(v.getContext().getApplicationContext(), NotificationService.class);
		if (enable){
			
			startService(i);
		}else{
			stopService(i);
		}
		
	}
});
}

};
