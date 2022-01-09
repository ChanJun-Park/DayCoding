package com.example.mynotificationexample.util

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.mynotificationexample.MainActivity
import com.example.mynotificationexample.R
import com.example.mynotificationexample.receiver.SnoozeReceiver

// Notification ID.
private const val NOTIFICATION_ID = 0
private const val REQUEST_CODE = 0
private const val FLAGS = 0

fun NotificationManager.sendNotification(message: String, applicationContext: Context) {

	val contentIntent = Intent(applicationContext, MainActivity::class.java)
	val contentPendingIntent = PendingIntent.getActivity(
		applicationContext,
		REQUEST_CODE,
		contentIntent,
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
		} else {
			PendingIntent.FLAG_UPDATE_CURRENT
		}
	)

	val eggImage = BitmapFactory.decodeResource(
		applicationContext.resources,
		R.drawable.cooked_egg
	)

	val notificationStyle = NotificationCompat.BigPictureStyle()
	notificationStyle.bigLargeIcon(null)
	notificationStyle.bigPicture(eggImage)

	val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
	val snoozePendingIntent = PendingIntent.getBroadcast(
		applicationContext,
		REQUEST_CODE,
		snoozeIntent,
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_ONE_SHOT
		} else {
			PendingIntent.FLAG_ONE_SHOT
		}
	)

	val builder = NotificationCompat.Builder(
		applicationContext,
		applicationContext.getString(R.string.egg_notification_channel_id)
	)

	builder.setSmallIcon(R.drawable.cooked_egg)
		.setContentTitle(applicationContext.getString(R.string.notification_title))
		.setContentText(message)
		.setContentIntent(contentPendingIntent)
		.setAutoCancel(true)
		.setLargeIcon(eggImage)
		.setStyle(notificationStyle)
		.setPriority(NotificationCompat.PRIORITY_HIGH)
		.addAction(
			R.drawable.egg_icon,
			applicationContext.getString(R.string.snooze),
			snoozePendingIntent
		)

	notify(NOTIFICATION_ID, builder.build())
}

fun NotificationManager.cancelNotifications() {
	cancelAll()
}