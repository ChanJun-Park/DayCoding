package com.jingom.myminipaint

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat

private const val STROKE_WIDTH = 12f

class MyCanvasView(context: Context) : View(context) {

//	private lateinit var extraBitmap: Bitmap
//	private lateinit var extraCanvas: Canvas

	private val backgroundColor = ResourcesCompat.getColor(resources, R.color.colorBackground, null)
	private val strokeColor = ResourcesCompat.getColor(resources, R.color.colorStroke, null)

	private val paint = Paint().apply {
		isAntiAlias = true
		style = Paint.Style.STROKE
		isDither = true
		color = strokeColor
		strokeCap = Paint.Cap.ROUND
		strokeJoin = Paint.Join.ROUND
		strokeWidth = STROKE_WIDTH
	}

	private val path = Path()
	private val drawingPath = Path()

	private val rectF = RectF()
	private val inset = 30f

	private var motionEventX = 0f
	private var motionEventY = 0f

	private var currentX = 0f
	private var currentY = 0f

	override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//		if (::extraBitmap.isInitialized) {
//			extraBitmap.recycle()
//		}
//
//		extraBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
//		extraCanvas = Canvas(extraBitmap)
//
//		extraCanvas.drawColor(backgroundColor)
//
//		rectF.apply {
//			left = 0 + inset
//			right = w - inset
//			top = 0 + inset
//			bottom = h - inset
//		}
//
//		extraCanvas.drawRect(rectF, paint)

		rectF.apply {
			left = 0 + inset
			right = w - inset
			top = 0 + inset
			bottom = h - inset
		}
	}

	override fun onDraw(canvas: Canvas) {
//		canvas.drawBitmap(extraBitmap, 0f, 0f, null)

		canvas.drawColor(backgroundColor)

		canvas.drawPath(drawingPath, paint)
		canvas.drawPath(path, paint)
	}

	override fun onTouchEvent(event: MotionEvent): Boolean {
		motionEventX = event.x
		motionEventY = event.y

		when (event.action) {
			MotionEvent.ACTION_DOWN -> onTouchStart()
			MotionEvent.ACTION_MOVE -> onTouchMove()
			MotionEvent.ACTION_UP -> onTouchUp()
		}

		currentX = motionEventX
		currentY = motionEventY
		return true
	}

	private fun onTouchStart() {
		path.reset()
		path.moveTo(motionEventX, motionEventY)
	}

	private fun onTouchMove() {
		path.quadTo(currentX, currentY, (motionEventX + currentX) / 2, (motionEventY + currentY) / 2)

//		extraCanvas.drawPath(path, paint)
		drawingPath.addPath(path)
		invalidate()
	}

	private fun onTouchUp() {
		path.reset()
	}
}