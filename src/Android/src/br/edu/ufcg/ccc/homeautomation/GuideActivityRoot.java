package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class GuideActivityRoot extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide_activity_root);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.guide_activity_root, menu);
		return true;
	}

}
