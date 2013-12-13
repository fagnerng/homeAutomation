package br.edu.ufcg.ccc.homeautomation.core.lbs;

import android.content.Context;
import android.os.Bundle;

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
		
		lbs.onConnected();
		
	}

	@Override
	public void onDisconnected() {

		
	}

}
