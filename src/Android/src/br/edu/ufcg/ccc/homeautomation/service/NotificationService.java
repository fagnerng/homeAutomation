package br.edu.ufcg.ccc.homeautomation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import br.edu.ufcg.ccc.homeautomation.core.lbs.LBSManager;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncResquestStatusDevs;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncVerifyStatusTask;


public class NotificationService extends Service {
	static AsyncResquestStatusDevs mTask;
	static Boolean enable;
	public static Context context;

	@Override
	public IBinder onBind(Intent intent) {
		return null;

	}

	@Override
	public void onCreate() {
		enable = true;
		context = getApplicationContext();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// For time consuming an long tasks you can launch a new thread here...
		LBSManager.getInstance(this).onStart();
		mTask = new AsyncResquestStatusDevs();
		mTask.execute();

	}

	public static void onEndResquest(String result) {
		
		new AsyncVerifyStatusTask(context, result).execute();
		if (enable) {
			mTask = new AsyncResquestStatusDevs();
			mTask.execute();
		}
	}

	@Override
	public void onDestroy() {
		LBSManager.getInstance(this).onStop();
		enable = false;
		mTask.cancel(true);

	}

}
