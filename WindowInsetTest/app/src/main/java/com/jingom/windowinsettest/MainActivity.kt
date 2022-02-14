package com.jingom.windowinsettest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.jingom.windowinsettest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	private var originStatusBarColor: Int = 0
	private var originNaviBarColor: Int = 0
	private var originAppearanceLightStatusBar: Boolean = false
	private var originAppearanceLightNaviBar: Boolean = false

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
		setContentView(binding.root)

		setOnClickListener()

		originStatusBarColor = window.statusBarColor
		originNaviBarColor = window.navigationBarColor

		val windowInsetsController = WindowInsetsControllerCompat(window, binding.root)
		originAppearanceLightStatusBar = windowInsetsController.isAppearanceLightStatusBars
		originAppearanceLightNaviBar = windowInsetsController.isAppearanceLightNavigationBars
	}

	private fun setOnClickListener() {
		binding.resetButton.setOnClickListener {
			window.apply {
				statusBarColor = originStatusBarColor
				navigationBarColor = originNaviBarColor
			}

			WindowInsetsControllerCompat(window, it).apply {
				isAppearanceLightStatusBars = originAppearanceLightStatusBar
				isAppearanceLightNavigationBars = originAppearanceLightNaviBar
			}
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
			val windowInsetsController = WindowInsetsControllerCompat(window, it)

			windowInsetsController.isAppearanceLightStatusBars = true
		}

		binding.statusBarTextColorOnDarkButton.setOnClickListener {
			val windowInsetsController = WindowInsetsControllerCompat(window, it)

			windowInsetsController.isAppearanceLightStatusBars = false
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
			val windowInsetsController = WindowInsetsControllerCompat(window, it)

			windowInsetsController.isAppearanceLightNavigationBars = true
		}

		binding.naviBarUiColorOnDarkButton.setOnClickListener {
			val windowInsetsController = WindowInsetsControllerCompat(window, it)

			windowInsetsController.isAppearanceLightNavigationBars = false
		}

	}

	private fun initCustomSystemUiColorSetting() {
		// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
		window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
	}
}