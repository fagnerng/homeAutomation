package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class PrefsActivity extends PreferenceActivity{
	 
@SuppressWarnings("deprecation")
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.preference);
   
}

};
