package br.edu.ufcg.ccc.homeautomation;

import br.edu.ufcg.ccc.homeautomation.core.lbs.LBSManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class LocationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		LBSManager.getInstance(this).onStart();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.location, menu);
		return true;
	}
@Override
protected void onStop() {
	LBSManager.getInstance(this).onStop();
	super.onStop();
	
}
}
