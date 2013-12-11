package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.app.Activity;
import android.view.Menu;

public class PrefsActivity extends PreferenceActivity{
	 
protected void onCreate(Bundle savedInstanceState) {
   super.onCreate(savedInstanceState);
   addPreferencesFromResource(R.xml.preference);
   
}

};
