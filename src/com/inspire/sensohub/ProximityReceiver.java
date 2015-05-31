package com.inspire.sensohub;

import java.util.Date;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.sax.StartElementListener;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class ProximityReceiver extends BroadcastReceiver {
	private static final int NOTIFICATION_ID = 1;
	long[] vibraPattern = { 0, 500, 250, 500 };
	MediaPlayer mediaPlayer;

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String key = LocationManager.KEY_PROXIMITY_ENTERING;

		boolean entering = intent.getBooleanExtra(key, false);
		NotificationManager notificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent notificationIntent = new Intent(context, MapActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.setAction(Intent.ACTION_MAIN);
		notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		NotificationCompat.Builder notification = new NotificationCompat.Builder(
				context)
				.setSmallIcon(R.drawable.notification)
				.setContentTitle("Proximity Alert")
				.setContentText("Destination Reached")
				.setAutoCancel(true)
				.setWhen(new Date().getTime())
				.setContentIntent(pendingIntent)
				.setDefaults(
						Notification.DEFAULT_VIBRATE
								| Notification.DEFAULT_LIGHTS).setSound(uri);
		if (entering) {
			notificationManager.cancelAll();
			notificationManager.notify(NOTIFICATION_ID, notification.build());
		} else {
			Log.d(getClass().getSimpleName(), "exiting");
		}

	}

}