package br.edu.ufcg.ccc.homeautomation.core.lbs;

import android.content.Context;
import android.location.Location;
import br.edu.ufcg.ccc.homeautomation.LocationActivity;

import com.google.android.gms.location.LocationListener;
public class MyLocationListener implements LocationListener {
	Context mContext;
	
	public MyLocationListener(Context context){
		mContext = context;
		//mContext = LocationActivity.mContext;
		
	}
	@Override
	public void onLocationChanged(Location arg0) {
		
		LocationActivity.et_lati.setText(arg0.getLatitude()+"");
		LocationActivity.et_long.setText(arg0.getLongitude()+"");
		LocationActivity.bt_save.setEnabled(true);
		
	}


}
