package com.example.mynotificationexample.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.mynotificationexample.R
import com.example.mynotificationexample.util.sendNotification

class AlarmReceiver: BroadcastReceiver() {

	override fun onReceive(context: Context, intent: Intent) {
//        Toast.makeText(context, context.getText(R.string.eggs_ready), Toast.LENGTH_SHORT).show()

		val notificationManager = ContextCompat.getSystemService(
			context,
			NotificationManager::class.java
		)
		notificationManager?.sendNotification(
			context.getString(R.string.eggs_ready),
			context
		)
	}

}