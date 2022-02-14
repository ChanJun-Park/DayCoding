package com.jingom.windowinsettest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.jingom.windowinsettest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	private var originStatusBarColor: Int = 0
	private var originNaviBarColor: Int = 0
	private var originSystemUiVisibility: Int = 0

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
		setContentView(binding.root)

		setOnClickListener()

		originStatusBarColor = window.statusBarColor
		originNaviBarColor = window.navigationBarColor
		originSystemUiVisibility = window.decorView.systemUiVisibility
	}

	private fun setOnClickListener() {
		binding.resetButton.setOnClickListener {
			window.statusBarColor = originStatusBarColor
			window.navigationBarColor = originNaviBarColor
			window.decorView.systemUiVisibility = originSystemUiVisibility
		}

		binding.statusBarColorRedButton.setOnClickListener {
			initCustomSystemUiColorSetting()

			window.statusBarColor = ContextCompat.getColor(this, android.R.color.holo_red_dark)
		}

		binding.statusBarColorTransparentButton.setOnClickListener {
			initCustomSystemUiColorSetting()

			window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
		}

		binding.statusBarTextColorOnLightButton.setOnClickListener {
			window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
		}

		binding.statusBarTextColorOnDarkButton.setOnClickListener {
			window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
		}

		binding.naviBarColorRedButton.setOnClickListener {
			initCustomSystemUiColorSetting()

			window.navigationBarColor = ContextCompat.getColor(this, android.R.color.holo_red_dark)
		}

		binding.naviBarColorTransparentButton.setOnClickListener {
			initCustomSystemUiColorSetting()

			window.navigationBarColor = ContextCompat.getColor(this, android.R.color.transparent)
		}

		binding.naviBarUiColorOnLightButton.setOnClickListener {
			window.decorView.systemUiVisibility = window.decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
		}

		binding.naviBarUiColorOnDarkButton.setOnClickListener {
			window.decorView.systemUiVisibility = window.decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
		}

	}

	private fun initCustomSystemUiColorSetting() {
		// clear FLAG_TRANSLUCENT_STATUS flag:
		window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

		// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
	}
}