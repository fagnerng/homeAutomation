package br.edu.ufcg.ccc.homeautomation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.core.lbs.LBSManager;
import br.edu.ufcg.ccc.homeautomation.networking.AssyncResquestStatusDevs;
import br.edu.ufcg.ccc.homeautomation.networking.AssyncVerifyStatusTask;


public class NotificationService extends Service {
	static AssyncResquestStatusDevs mTask;
	static Boolean enable;
	public static Context context;

	@Override
	public IBinder onBind(Intent intent) {
		return null;

	}

	@Override
	public void onCreate() {
		Toast.makeText(this, "The new Service was Created", Toast.LENGTH_LONG)
				.show();
		enable = true;
		context = getApplicationContext();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// For time consuming an long tasks you can launch a new thread here...
		Toast.makeText(this, " Service Started", Toast.LENGTH_LONG).show();
		LBSManager.getInstance(this).onStart();

		mTask = new AssyncResquestStatusDevs();
		mTask.execute();

	}

	public static void onEndResquest(String result) {
		
		new AssyncVerifyStatusTask(context, result).execute();
		if (enable) {
			mTask = new AssyncResquestStatusDevs();
			mTask.execute();
		}
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
		LBSManager.getInstance(this).onStop();
		enable = false;
		mTask.cancel(true);

	}

}
