package br.edu.ufcg.ccc.homeautomation.core.lbs;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;

public class LBSManager {
	private static LocationClient mLocationClient;
	private static Location mCurrentLocation;
	private static LocationRequest mLocationRequest;
	private static MyLocationListener mListener;
	private static Context mContext;

	public  LocationClient getLocationClient() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(
					mContext.getApplicationContext(),
					new MyConnectionCallbacks(mContext),
					new MyOnConnectionFailedListener());

		}
		return mLocationClient;
	}

	public  Location getCurrentLocation() {
		return mCurrentLocation;
	}

	public  LocationRequest getLocationRequest() {
		if (mLocationRequest == null) {
			mLocationRequest = LocationRequest.create();

		}
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(5000);
		mLocationRequest.setFastestInterval(1000);
		return mLocationRequest;
	}

	public  LocationRequest getLocationRequest(int priority,
			int interval, int fastInterval) {
		if (mLocationRequest == null) {
			mLocationRequest = LocationRequest.create();

		}
		mLocationRequest.setPriority(priority);
		mLocationRequest.setInterval(interval);
		mLocationRequest.setFastestInterval(fastInterval);
		return mLocationRequest;
	}

	public  MyLocationListener getListener() {
		if (mListener == null){
			mListener = new MyLocationListener(mContext);
		}
		return mListener;
	}

	private static LBSManager instance = new LBSManager();;



	public static synchronized LBSManager getInstance(Context context) {
		if (instance == null) {
			instance = new LBSManager();
			
		}
		mContext = context;
		return instance;
	}

	private  void setCurrentLocation() {
		mCurrentLocation =  getLocationClient().getLastLocation();
		
	}

	private  void setLocationClient() {
		getLocationClient().requestLocationUpdates(getLocationRequest(LocationRequest.PRIORITY_HIGH_ACCURACY, 30000,10000),getListener() );
		
	}

	public  void rmLocationClient() {
		getLocationClient().removeLocationUpdates(getListener());
		
	}
	
	/**
	 * 
	 */
	public void onConnected(){
		setCurrentLocation();
		setLocationClient();
	}
	public void onStart(){
		LBSManager.getInstance(mContext).getLocationClient().connect();
	}
	
	public void onStop(){
		LBSManager.getInstance(mContext).rmLocationClient();
		LBSManager.getInstance(mContext).getLocationClient().disconnect();
	}

}
