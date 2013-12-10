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
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

public class GuideActivityRoot extends Activity {

	ImageButton rootDevices;
	ImageButton rootProfile;
	ImageButton rootUsers;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity_root);
		
		final Animation scale = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.reversed_scale);
		
		rootProfile = (ImageButton) findViewById(R.id.icon_button_root);
		rootDevices = (ImageButton) findViewById(R.id.device_button_root);
		rootUsers = (ImageButton) findViewById(R.id.user_button_root);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		boolean status = prefs.getBoolean("distance",true);
		System.err.println(status);
		
		rootProfile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				setAnimation(scale, intent, 1);
				
				v.startAnimation(scale);
				
				
				

				
			}
		});
		
		rootDevices.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				intent.putExtra("tab", 0);
				setAnimation(scale, intent, 0);
				
				v.startAnimation(scale);
			}


		});
		
		rootUsers.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
				setAnimation(scale, intent, 2);
				v.startAnimation(scale);
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
	
	private void setAnimation(final Animation scale, final Intent intent, final int tab) {
		scale.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				intent.putExtra("tab", tab);
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				startActivity(intent);
				
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		Intent i = new Intent(getApplicationContext(),LoginActivity.class);
		startActivity(i);
		super.onBackPressed();
	}
}




