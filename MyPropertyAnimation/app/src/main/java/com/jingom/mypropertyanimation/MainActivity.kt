package com.jingom.mypropertyanimation

import android.animation.*
import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Interpolator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

class MainActivity : AppCompatActivity() {

	lateinit var star: ImageView
	lateinit var rotateButton: Button
	lateinit var translateButton: Button
	lateinit var scaleButton: Button
	lateinit var fadeButton: Button
	lateinit var colorizeButton: Button
	lateinit var showerButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		star = findViewById(R.id.star)
		rotateButton = findViewById(R.id.rotateButton)
		translateButton = findViewById(R.id.translateButton)
		scaleButton = findViewById(R.id.scaleButton)
		fadeButton = findViewById(R.id.fadeButton)
		colorizeButton = findViewById(R.id.colorizeButton)
		showerButton = findViewById(R.id.showerButton)

		rotateButton.setOnClickListener {
			rotater()
		}

		translateButton.setOnClickListener {
			translater()
		}

		scaleButton.setOnClickListener {
			scaler()
		}

		fadeButton.setOnClickListener {
			fader()
		}

		colorizeButton.setOnClickListener {
			colorizer()
		}

		showerButton.setOnClickListener {
			shower()
		}
	}

	private fun ObjectAnimator.disableButtonDuringAnimation(button: Button) {
		addListener(object : AnimatorListenerAdapter() {
			override fun onAnimationStart(animation: Animator?) {
				button.isEnabled = false
			}

			override fun onAnimationEnd(animation: Animator?) {
				button.isEnabled = true
			}
		})
	}

	private fun rotater() {
		val animator = ObjectAnimator.ofFloat(star, View.ROTATION, -360f, 0f)
		animator.disableButtonDuringAnimation(rotateButton)
		animator.duration = 1000
		animator.start()
	}

	private fun translater() {
		val animator = ObjectAnimator.ofFloat(star, View.TRANSLATION_X, 300f)
		animator.disableButtonDuringAnimation(translateButton)
		animator.repeatCount = 1
		animator.repeatMode = ObjectAnimator.REVERSE
		animator.start()
	}

	private fun scaler() {
		val propertyX = PropertyValuesHolder.ofFloat(View.SCALE_X, 3f)
		val propertyY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 3f)

		val animator = ObjectAnimator.ofPropertyValuesHolder(star, propertyX, propertyY)
		animator.disableButtonDuringAnimation(scaleButton)
		animator.repeatCount = 1
		animator.repeatMode = ObjectAnimator.REVERSE
		animator.start()
	}

	private fun fader() {
		val animator = ObjectAnimator.ofFloat(star, View.ALPHA, 0f)
		animator.disableButtonDuringAnimation(fadeButton)
		animator.repeatCount = 1
		animator.repeatMode = ObjectAnimator.REVERSE
		animator.start()
	}

	@SuppressLint("ObjectAnimatorBinding")
	private fun colorizer() {
		val animator = ObjectAnimator.ofArgb(star.parent as View, "backgroundColor", Color.BLACK, Color.RED)
		animator.disableButtonDuringAnimation(colorizeButton)
		animator.repeatCount = 1
		animator.repeatMode = ObjectAnimator.REVERSE
		animator.start()
	}

	private fun shower() {
		val container = star.parent as ViewGroup
		val containerWidth = container.width
		val containerHeight = container.height
		var starW = star.width.toFloat()
		var starH = star.height.toFloat()

		for (index in 0..100) {
			val newStar = ImageView(this)
			newStar.setImageResource(R.drawable.ic_star)
			newStar.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			container.addView(newStar)

			newStar.scaleX = Math.random().toFloat() * 1.5f + 0.1f
			newStar.scaleY = newStar.scaleX
			starW *= newStar.scaleX
			starH *= newStar.scaleY

			val xPosition = Math.random().toFloat() * containerWidth - starW / 2
			newStar.translationX = xPosition

			val rotationAnimator = ObjectAnimator.ofFloat(newStar, View.ROTATION, 0f, (Math.random() * 1080).toFloat())
			rotationAnimator.interpolator = LinearInterpolator()

			val transitionAnimator = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerHeight + starH)
			transitionAnimator.interpolator = AccelerateInterpolator(1f)

			val animatorSet = AnimatorSet()
			animatorSet.playTogether(rotationAnimator, transitionAnimator)
			animatorSet.duration = (Math.random() * 1500 + 500).toLong()
			animatorSet.addListener(object : AnimatorListenerAdapter() {
				override fun onAnimationEnd(animation: Animator?) {
					container.removeView(newStar)
				}
			})
			animatorSet.start()
		}
	}

}
