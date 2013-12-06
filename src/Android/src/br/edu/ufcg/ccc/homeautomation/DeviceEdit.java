package br.edu.ufcg.ccc.homeautomation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import br.edu.ufcg.ccc.homeautomation.entities.Device;

public class DeviceEdit extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Device mDev = (Device) getIntent().getExtras().getSerializable("editDevice");
		setContentView(R.layout.teste_activity_device_edit);
		TextView deviceName = (TextView) findViewById(R.id.tv_device_name);
		deviceName.setText(mDev.getName());
		
		
		
/*		EditText etEditTaskName = (EditText) findViewById(R.id.et_edit_task);
		TextView tvStatusDev = (TextView) findViewById(R.id.tv_status_device);
		etEditTaskName.setText(mDev.getName());
		System.out.println("usuario eh root?: " + UserManager.getInstance().getUserObject().isAdmin());
		etEditTaskName.setEnabled(UserManager.getInstance().getUserObject().isAdmin());
		tvStatusDev.setText(mDev.getStatus());*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device_edit, menu);
		return true;
	}

}
