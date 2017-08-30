package com.diabeticsCare.diabetico;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;

@SuppressLint("NewApi")
public class Reminder extends BroadcastReceiver{
/* To Be Resolved **/
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@SuppressLint("NewApi")
	@Override
	public void onReceive(Context context, Intent intent) {

		Calendar now = GregorianCalendar.getInstance();
		int dayOfWeek = now.get(Calendar.DATE);
				
		if(dayOfWeek != 5 && dayOfWeek != 7) {
			
			Intent resultIntent = new Intent(context, UserProfile.class);
			
			String title 			= intent.getStringExtra("Title");
			String Msg   			= intent.getExtras().getString("Msg");
			int    iNotificationId	= intent.getExtras().getInt("notifyId");
			
			Notification.Builder mBuilder = 
					new Notification.Builder(context)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle(title)
			.setContentText(Msg)
			.setDefaults(Notification.DEFAULT_VIBRATE)
			.setSound( RingtoneManager.getDefaultUri( RingtoneManager.TYPE_NOTIFICATION ) );
			
			
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			stackBuilder.addParentStack(MainActivity.class);
			stackBuilder.addNextIntent(resultIntent);
			PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
			mBuilder.setContentIntent(resultPendingIntent);
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			mNotificationManager.notify(iNotificationId, mBuilder.build());
		}
	}
}
