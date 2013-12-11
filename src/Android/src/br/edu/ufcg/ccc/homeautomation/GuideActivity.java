package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

public class GuideActivity extends Activity {

	
	ImageButton devices;
	ImageButton profile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		
		devices = (ImageButton) findViewById(R.id.device_button);
		profile = (ImageButton) findViewById(R.id.user_button);
		
		final Animation scale = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.reversed_scale);
		
		devices.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
				setAnimation(scale, intent, 0);
				v.startAnimation(scale);
				
			}
		});
		
		profile.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), ChildActivity.class);
				setAnimation(scale, intent, 1);
				v.startAnimation(scale);
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide, menu);
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
