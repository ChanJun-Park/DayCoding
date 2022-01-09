package com.example.mynotificationexample.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mynotificationexample.R
import com.example.mynotificationexample.databinding.FragmentEggTimerBinding
import com.google.firebase.messaging.FirebaseMessaging

class EggTimerFragment : Fragment() {

	private val TOPIC = "breakfast"

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

		val binding: FragmentEggTimerBinding = FragmentEggTimerBinding.inflate(inflater, container, false)

		val viewModel = ViewModelProvider(this).get(EggTimerViewModel::class.java)

		binding.eggTimerViewModel = viewModel
		binding.lifecycleOwner = viewLifecycleOwner

		createChannel(
			getString(R.string.egg_notification_channel_id),
			getString(R.string.egg_notification_channel_name)
		)

		createChannel(
			getString(R.string.breakfast_notification_channel_id),
			getString(R.string.breakfast_notification_channel_name)
		)

		subscribeTopic()

		return binding.root
	}

	private fun subscribeTopic() {
		FirebaseMessaging.getInstance()
			.subscribeToTopic(TOPIC)
			.addOnCompleteListener { task ->
				var msg = getString(R.string.message_subscribed)
				if (!task.isSuccessful) {
					msg = getString(R.string.message_subscribe_failed)
				}
				Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
			}
	}

	private fun createChannel(channelId: String, channelName: String) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val notificationChannel = NotificationChannel(
				channelId,
				channelName,
				NotificationManager.IMPORTANCE_HIGH
			).apply {
				enableLights(true)
				lightColor = Color.RED
				enableVibration(true)
				description = "Time for breakfast"
				setShowBadge(true)
			}

			val notificationManager = requireActivity().getSystemService(NotificationManager::class.java)
			notificationManager.createNotificationChannel(notificationChannel)
		}

	}

	companion object {
		fun newInstance() = EggTimerFragment()
	}
}