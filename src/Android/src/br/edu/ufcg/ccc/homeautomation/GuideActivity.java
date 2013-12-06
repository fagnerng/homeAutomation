package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.view.View;

public class GuideActivity extends Activity {

	
	ImageButton devices;
	ImageButton profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		devices = (ImageButton) findViewById(R.id.device_button);
		profile = (ImageButton) findViewById(R.id.user_button);
		
		devices.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				intent.putExtra("tab", 0);
				startActivity(intent);
				
			}
		});
		
		profile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				intent.putExtra("tab", 1);
				startActivity(intent);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide, menu);
		return true;
	}

}
