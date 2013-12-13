package br.edu.ufcg.ccc.homeautomation.core.lbs;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

public class LBSManager {
	private LocationClient mLocationClient;
	private Location mCurrentLocation;
	private LocationRequest mLocationRequest;
	private LocationListener mListener;
	private int interval;
	private int fastinterval;

	public void setListener(LocationListener locationListener) {
		this.mListener = locationListener;
	}

	private  Context mContext;

	public LBSManager(Context context) {
		this.mContext = context;
		this.interval = 50000;
		this.fastinterval = 10000;
	}

	public LBSManager(Context context, int interval, int fastInterval) {
		this.mContext = context;
		this.interval = interval;
		this.fastinterval = fastInterval;
	}

	public LocationClient getLocationClient() {
		if (mLocationClient == null) {
			mLocationClient = new LocationClient(
					mContext,
					new MyConnectionCallbacks(mContext, this),
					new MyOnConnectionFailedListener());

		}
		return mLocationClient;
	}

	public Location getCurrentLocation() {
		return mCurrentLocation;
	}

	public LocationRequest getLocationRequest() {
		if (mLocationRequest == null) {
			mLocationRequest = LocationRequest.create();

		}
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(interval);
		mLocationRequest.setFastestInterval(fastinterval);
		return mLocationRequest;
	}

	public LocationRequest getLocationRequest(int priority, int interval,
			int fastInterval) {
		if (mLocationRequest == null) {
			mLocationRequest = LocationRequest.create();

		}
		mLocationRequest.setPriority(priority);
		mLocationRequest.setInterval(interval);
		mLocationRequest.setFastestInterval(fastInterval);
		return mLocationRequest;
	}

	public LocationListener getListener() {
		if (mListener == null) {
			mListener = new MyLocationListener(mContext);
		}
		return mListener;
	}

	/*
	 * public static synchronized LBSManager getInstance(Context context) { if
	 * (instance == null) { instance = new LBSManager();
	 * 
	 * } mContext = context; return instance; }
	 */

	private void setCurrentLocation() {
		mCurrentLocation = getLocationClient().getLastLocation();

	}

	private void setLocationClient() {
		getLocationClient().requestLocationUpdates(
				getLocationRequest(LocationRequest.PRIORITY_HIGH_ACCURACY,
						interval, fastinterval), getListener());

	}

	public void rmLocationClient() {
		getLocationClient().removeLocationUpdates(getListener());

	}

	/**
	 * 
	 */
	public void onConnected() {
		setCurrentLocation();
		setLocationClient();
	}

	public void onStart() {
		this.getLocationClient().connect();
	}

	public void onStop() {
		this.rmLocationClient();
		this.getLocationClient().disconnect();
		mLocationClient = null;
		mCurrentLocation = null;
		mLocationRequest = null;
		mContext = null;
	}

}
