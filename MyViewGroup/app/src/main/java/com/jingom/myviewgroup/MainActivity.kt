package com.jingom.myviewgroup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import java.util.*

class MainActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		val timeZone = TimeZone.getTimeZone("한국 표준시")

		val flowLayout: FlowLayout = findViewById(R.id.flowLayout)
		val layoutInflater = LayoutInflater.from(this)
		for (index in 0 until 21) {
			val view = layoutInflater.inflate(R.layout.item, flowLayout, false) as ViewGroup
			view.layoutParams = FlowLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)


			when (index) {
				20 -> {
					(view.getChildAt(0) as TextView).text = "tes"
				}
				19 -> {
					(view.getChildAt(0) as TextView).text = "testtesttes"
				}
				else -> {
					(view.getChildAt(0) as TextView).text = "test"
				}
			}

			flowLayout.addView(view)
		}

//		val myViewGroup: MyViewGroup = findViewById(R.id.myViewGroup)
//		val textView = TextView(this)
//		textView.layoutParams = ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
//		textView.text = "test"
//
//		myViewGroup.addView(textView)
	}
}