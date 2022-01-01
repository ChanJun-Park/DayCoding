package com.jingom.mycustomfancontroller

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.withStyledAttributes
import androidx.core.graphics.TypefaceCompat
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private enum class FanSpeed(@StringRes val label: Int) {
	OFF(R.string.off),
	LOW(R.string.low),
	MEDIUM(R.string.medium),
	HIGH(R.string.high);

	fun next() = when(this) {
		OFF -> LOW
		LOW -> MEDIUM
		MEDIUM -> HIGH
		HIGH -> OFF
	}
}

private const val RADIUS_LABEL_OFFSET = 30
private const val RADIUS_INDICATOR_OFFSET = -35

class DialView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

	private var radius: Float = 0f
	private var pointF = PointF(0f, 0f)
	private var speed = FanSpeed.OFF

	private var offSpeedColor: Int = 0
	private var lowSpeedColor: Int = 0
	private var mediumSpeedColor: Int = 0
	private var highSpeedColor: Int = 0

	private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
		style = Paint.Style.FILL
		textAlign = Paint.Align.CENTER
		textSize = 55.0f
		typeface = Typeface.create( "", Typeface.BOLD)
	}

	private fun PointF.computeXYPosition(radius: Float, fanSpeed: FanSpeed) {
		val startAngle = (Math.PI * 9) / 8
		val angle = startAngle + (Math.PI * fanSpeed.ordinal) / 4

		x = (radius * cos(angle) + width / 2).toFloat()
		y = (radius * sin(angle) + height / 2).toFloat()
	}

	init {
		isClickable = true
		context.withStyledAttributes(attrs, R.styleable.DialView) {
			offSpeedColor = getColor(R.styleable.DialView_offSpeedColor, 0)
			lowSpeedColor = getColor(R.styleable.DialView_lowSpeedColor, 0)
			mediumSpeedColor = getColor(R.styleable.DialView_mediumSpeedColor, 0)
			highSpeedColor = getColor(R.styleable.DialView_highSpeedColor, 0)
		}
	}

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
		radius = (min(w, h) / 2 * 0.8).toFloat()
	}

	override fun performClick(): Boolean {
		if (super.performClick()) {
			return true
		}

		speed = speed.next()
		contentDescription = resources.getString(speed.label)

		invalidate()

		return true
	}

	override fun onDraw(canvas: Canvas) {
		val cx = width.toFloat() / 2
		val cy = height.toFloat() / 2

		when (speed) {
			FanSpeed.OFF -> paint.color = offSpeedColor
			FanSpeed.LOW -> paint.color = lowSpeedColor
			FanSpeed.MEDIUM -> paint.color = mediumSpeedColor
			FanSpeed.HIGH -> paint.color = highSpeedColor
		}
		canvas.drawCircle(cx, cy, radius, paint)

		pointF.computeXYPosition(radius + RADIUS_INDICATOR_OFFSET, speed)

		paint.color = Color.BLACK
		canvas.drawCircle(pointF.x, pointF.y, radius / 8, paint)

		FanSpeed.values().forEach {
			pointF.computeXYPosition(radius + RADIUS_LABEL_OFFSET, it)

			canvas.drawText(resources.getString(it.label), pointF.x, pointF.y, paint)
		}
	}
}