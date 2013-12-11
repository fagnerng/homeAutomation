package br.edu.ufcg.ccc.homeautomation.networking;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import br.edu.ufcg.ccc.homeautomation.LoginActivity;

import br.edu.ufcg.ccc.homeautomation.R;

public class AssycMakeNotification extends AsyncTask<Void, Void, Void>{
	int idTask;
	Context mContext;
	String mText;
	public AssycMakeNotification(Context context, int id, String msgText){
		this.mContext = context;
		this.idTask = id;
		this.mText= msgText; 
	}
	@Override
	protected Void doInBackground(Void... params) {
		if (mContext != null){
		Intent notificationIntent = new Intent(mContext, LoginActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(mContext,
		        200, notificationIntent,
		        PendingIntent.FLAG_CANCEL_CURRENT);

		NotificationManager nm = (NotificationManager) mContext
		        .getSystemService(Context.NOTIFICATION_SERVICE);

		Resources res = mContext.getResources();
		Notification.Builder builder = new Notification.Builder(mContext);

		builder.setContentIntent(contentIntent)
		            .setSmallIcon(R.drawable.ic_launcher)
		            .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher))
		            .setTicker(res.getString(R.string.app_name))
		            .setWhen(System.currentTimeMillis())
		            .setAutoCancel(true)
		            .setContentTitle(res.getString(R.string.app_name))
		            .setContentText(mText + ": esta ligado");
		builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
		builder.setVibrate(new long[] {100,200,100,200});
		Notification n = builder.build();

		nm.notify(idTask, n);
		}
		return null;
	}

}
