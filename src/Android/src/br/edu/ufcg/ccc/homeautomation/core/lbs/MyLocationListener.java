package br.edu.ufcg.ccc.homeautomation.core.lbs;

import android.content.Context;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
public class MyLocationListener implements LocationListener {
	Context mContext;
	public MyLocationListener(Context context){
		mContext = context;
	}
	@Override
	public void onLocationChanged(Location arg0) {
		
		/// TODO aqui deve ser feito o deve ser feito
		//verificar se esta longe de casa e notificar o usuario
		Toast.makeText(mContext,  String.format("%.2f", MathLab.distancia(arg0))+" m", Toast.LENGTH_SHORT).show();
	}
	
	public static class MathLab {
		static Location lastLocation;
		
		public static void setHomeLocation(Location location){
			lastLocation = location;
		}
		
		public static double distancia(Location currentLocation) {
			if (lastLocation == null) {
				lastLocation = currentLocation;

			}
			double latitudeInicial = lastLocation.getLatitude();
			double longitudeInicial = lastLocation.getLongitude();
			double latitudeFinal = currentLocation.getLatitude();
			double longitudeFinal = currentLocation.getLongitude();
			int raioTerra = 6371;
			double PI = Math.PI;
			int valorMetade = 90;
			double v1 = Math.cos(PI * (valorMetade - latitudeFinal) / 180);
			double v2 = Math.cos((valorMetade - latitudeInicial) * PI / 180);
			double v3 = Math.sin((valorMetade - latitudeFinal) * PI / 180);
			double v4 = Math.sin((valorMetade - latitudeInicial) * PI / 180);
			double v5 = Math.cos((longitudeInicial - longitudeFinal) * PI / 180);

			double result = raioTerra * Math.acos((v1 * v2) + (v3 * v4 * v5));

			return result * 1000;
		}
	}

}
