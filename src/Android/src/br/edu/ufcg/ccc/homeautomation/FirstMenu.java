package br.edu.ufcg.ccc.homeautomation;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class FirstMenu extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_menu);
		
		findViewById(R.id.bt1_menu).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstMenu.this, MainActivity.class);
				intent.putExtra("tab", 0);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.bt2_menu).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstMenu.this, MainActivity.class);
				intent.putExtra("tab", 1);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.bt3_menu).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstMenu.this, MainActivity.class);
				intent.putExtra("tab", 2);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.bt4_menu).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstMenu.this, MainActivity.class);
				intent.putExtra("tab", 3);
				startActivity(intent);
			}
		});
		
		findViewById(R.id.bt5_menu).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FirstMenu.this, MainActivity.class);
				intent.putExtra("tab", 4);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_menu, menu);
		return true;
	}

}
