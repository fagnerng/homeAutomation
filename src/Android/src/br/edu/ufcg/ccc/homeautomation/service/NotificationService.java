package br.edu.ufcg.ccc.homeautomation.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.core.lbs.LBSManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncResquestStatusDevs;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncVerifyStatusTask;

import com.google.android.gms.location.LocationListener;

public class NotificationService extends Service {
	static int MILISECOND = 1000;
	static int SECOND = 60 * MILISECOND;
	static int MINUTE = 60 * SECOND;
	static int HOUR = 60 * MINUTE;
	static int DAY = 24 * HOUR;
	static AsyncResquestStatusDevs mTask;
	static Boolean enable;
	public static Context context;
	private LBSManager mLBS;
	private static boolean away;

	@Override
	public IBinder onBind(Intent intent) {
		return null;

	}

	@Override
	public void onCreate() {
		enable = true;
		context = getApplicationContext();
		MathLab.setHomeLocation(UserManager.getInstance().getUserObject()
				.getLatitude(), UserManager.getInstance().getUserObject()
				.getLongitude());
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// For time consuming an long tasks you can launch a new thread here...
		Toast.makeText(context, "start", Toast.LENGTH_SHORT).show();
		mLBS = new LBSManager(context, HOUR, 30 * MINUTE);

		mLBS.setListener(new LocationListener() {

			@Override
			public void onLocationChanged(Location arg0) {
				Toast.makeText(context, MathLab.distancia(arg0) + "m",
						Toast.LENGTH_SHORT).show();
				away = MathLab.distancia(arg0)>20;
				mTask = new AsyncResquestStatusDevs();
				mTask.execute();

			}
		});
		mLBS.onStart();

	}

	public static void onEndResquest(String result) {

		new AsyncVerifyStatusTask(context, result,away).execute();

	}

	@Override
	public void onDestroy() {
		mLBS.onStop();
		enable = false;
		mTask.cancel(true);

	}

	public static class MathLab {
		public static double latitude = Double.NaN;
		public static double longitude = Double.NaN;

		public static void setHomeLocation(double latitude, double longitude) {
			MathLab.latitude = latitude;
			MathLab.longitude = longitude;
		}

		public static double distancia(Location currentLocation) {
			if (MathLab.latitude == Double.NaN) {
				return 0;
			}
			double latitudeInicial = MathLab.latitude;
			double longitudeInicial = MathLab.longitude;
			double latitudeFinal = currentLocation.getLatitude();
			double longitudeFinal = currentLocation.getLongitude();
			int raioTerra = 6371;
			double PI = Math.PI;
			int valorMetade = 90;
			double v1 = Math.cos(PI * (valorMetade - latitudeFinal) / 180);
			double v2 = Math.cos((valorMetade - latitudeInicial) * PI / 180);
			double v3 = Math.sin((valorMetade - latitudeFinal) * PI / 180);
			double v4 = Math.sin((valorMetade - latitudeInicial) * PI / 180);
			double v5 = Math
					.cos((longitudeInicial - longitudeFinal) * PI / 180);

			double result = raioTerra * Math.acos((v1 * v2) + (v3 * v4 * v5));

			return result * 1000;
		}
	}

}
