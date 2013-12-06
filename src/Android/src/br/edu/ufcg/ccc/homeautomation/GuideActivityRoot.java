package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View;
import android.widget.ImageButton;

public class GuideActivityRoot extends Activity {

	ImageButton rootDevices;
	ImageButton rootProfile;
	ImageButton rootUsers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity_root);
		
		rootProfile = (ImageButton) findViewById(R.id.icon_button_root);
		rootDevices = (ImageButton) findViewById(R.id.device_button_root);
		rootUsers = (ImageButton) findViewById(R.id.user_button_root);
		
		rootProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				intent.putExtra("tab", 1);
				startActivity(intent);
				
			}
		});
		
		rootDevices.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				intent.putExtra("tab", 0);
				startActivity(intent);
			}
		});
		
		rootUsers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				intent.putExtra("tab", 2);
				startActivity(intent);
			}
		});
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide_activity_root, menu);
		menu.getItem(0).setOnMenuItemClickListener(new OnMenuItemClickListener() {
			
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent i = new Intent(getApplicationContext(),PrefsActivity.class);
				startActivity(i);
				return false;
			}
		});
		return true;
	}
}




