package br.edu.ufcg.ccc.homeautomation.core.lbs;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesClient;

public class MyConnectionCallbacks implements GooglePlayServicesClient.ConnectionCallbacks{

	Context mContext;
	LBSManager lbs;
	public MyConnectionCallbacks(Context context, LBSManager lbs){
		mContext = context;
		this.lbs = lbs;
	}
	@Override
	public void onConnected(Bundle arg0) {
		Toast.makeText(mContext, "Connected", Toast.LENGTH_SHORT).show();
		
		lbs.onConnected();
		
	}

	@Override
	public void onDisconnected() {
		Toast.makeText(mContext, "Disconnected. Please re-connect.",
				Toast.LENGTH_SHORT).show();
	}

}
