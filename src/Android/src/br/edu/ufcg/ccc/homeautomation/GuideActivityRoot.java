package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
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
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("tab", 0);
				startActivity(intent);
				
			}
		});
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide_activity_root, menu);
		return true;
	}

}
