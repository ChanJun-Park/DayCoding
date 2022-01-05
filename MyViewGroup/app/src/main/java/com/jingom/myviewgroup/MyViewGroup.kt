package com.jingom.myviewgroup

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup

class MyViewGroup @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

	init {
		val view = View(context)
		addView(view)
	}

	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

	}

	override fun addView(child: View) {
		super.addView(child)
		Log.d("MyViewGroup", "child count : $childCount")
	}
}