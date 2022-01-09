package com.example.mynotificationexample

import android.app.NotificationManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.mynotificationexample.util.sendNotification
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

	override fun onMessageReceived(remoteMessage: RemoteMessage) {
		// Not getting messages here? See why this may be: https://goo.gl/39bRNJ
		Log.d(TAG, "From: ${remoteMessage.from}")

		// TODO Step 3.5 check messages for data
		remoteMessage.data.let {
			Log.d(TAG, "Message data payload: $it")
		}

		remoteMessage.notification?.let { notification ->
			notification.body?.let {
				sendNotification(it)
			}
		}
	}

	override fun onNewToken(token: String) {
		Log.d(TAG, "token : $token")

		sendRegistrationToServer(token)
	}

	private fun sendRegistrationToServer(token: String) {

	}

	/**
	 * Create and show a simple notification containing the received FCM message.
	 *
	 * @param messageBody FCM message body received.
	 */
	private fun sendNotification(messageBody: String) {
		val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
		notificationManager.sendNotification(messageBody, applicationContext)
	}

	companion object {
		private const val TAG = "MyFirebaseMsgService"
	}
}