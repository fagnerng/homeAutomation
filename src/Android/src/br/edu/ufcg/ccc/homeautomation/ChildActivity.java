package br.edu.ufcg.ccc.homeautomation;

import br.edu.ufcg.ccc.homeautomation.entities.DeviceAdapter;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.listener.OnClickDeviceList;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.widget.ListView;

public class ChildActivity extends Activity {
	private User mUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_child);
		mUser = UserManager.getInstance().getUserObject();
		ListView lv_devices = (ListView)findViewById(R.id.lv_devices_child);
		DeviceAdapter devAdapter = new DeviceAdapter(this,mUser.getDevices() );
		lv_devices.setOnItemClickListener(new OnClickDeviceList());
		lv_devices.setAdapter(devAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.child, menu);
		return true;
	}

}
