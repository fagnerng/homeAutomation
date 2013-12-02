package br.edu.ufcg.ccc.homeautomation.listener;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import br.edu.ufcg.ccc.homeautomation.DeviceEdit;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.entities.DeviceAdapter;

public class OnClickDeviceList implements AdapterView.OnItemClickListener{

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Device mDev = (Device) ((DeviceAdapter) parent.getAdapter()).getItem(position);
		Intent mIntent = new Intent(parent.getContext(), DeviceEdit.class);
		mIntent.putExtra("editDevice", mDev);
		parent.getContext().startActivity(mIntent);
		
	}

	

}
