package com.example.mynotificationexample.receiver

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import android.text.format.DateUtils
import androidx.core.app.AlarmManagerCompat
import androidx.core.content.ContextCompat
import com.example.mynotificationexample.util.cancelNotifications

private const val REQUEST_CODE = 0

class SnoozeReceiver: BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		val notificationManager = ContextCompat.getSystemService(
			context,
			NotificationManager::class.java
		)

		notificationManager?.cancelNotifications()

		val triggerTime = SystemClock.elapsedRealtime() + DateUtils.MINUTE_IN_MILLIS

		val notifyIntent = Intent(context, AlarmReceiver::class.java)
		val notifyPendingIntent = PendingIntent.getBroadcast(
			context,
			REQUEST_CODE,
			notifyIntent,
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
			} else {
				PendingIntent.FLAG_UPDATE_CURRENT
			}
		)
		val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		AlarmManagerCompat.setExactAndAllowWhileIdle(
			alarmManager,
			AlarmManager.ELAPSED_REALTIME_WAKEUP,
			triggerTime,
			notifyPendingIntent
		)
	}
}