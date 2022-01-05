package com.jingom.myviewgroup

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import java.util.ArrayList
import kotlin.math.max

private const val NOT_CONSTRAINT_LINE_COUNT = -1
private const val COUNT_VIEW_TAG = "count_view"

class FlowLayout @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyle: Int = 0
) : ViewGroup(context, attrs, defStyle) {

	private var maxLineCount = NOT_CONSTRAINT_LINE_COUNT
	var gravity = Gravity.START or Gravity.TOP
		set(value) {
			var gravity = value

			if (field != gravity) {
				if (gravity and Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK == 0) {
					gravity = gravity or Gravity.START
				}

				if (gravity and Gravity.VERTICAL_GRAVITY_MASK == 0) {
					gravity = gravity or Gravity.TOP
				}

				field = gravity

				requestLayout()
			}
		}

	private val lineList: MutableList<MutableList<View>> = ArrayList()
	private val lineHeightList: MutableList<Int> = ArrayList()
	private val lineMarginList: MutableList<Int> = ArrayList()

	init {
		context.withStyledAttributes(attrs, R.styleable.FlowLayout, defStyle, 0) {
			val index = getInt(R.styleable.FlowLayout_android_gravity, -1)
			if (index > 0) {
				gravity = index
			}

			maxLineCount = getInt(R.styleable.FlowLayout_maxLineCount, NOT_CONSTRAINT_LINE_COUNT)

			if (maxLineCount != NOT_CONSTRAINT_LINE_COUNT) {
				addHiddenItemCountView()
			}
		}
	}

	private fun addHiddenItemCountView() {
		val countTextView = TextView(context).apply {
			setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.5f)
			setTextColor(ResourcesCompat.getColor(context.resources, android.R.color.darker_gray, null))
			text = "+0"

			val params = LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
			params.gravity = Gravity.CENTER_VERTICAL
			layoutParams = params

			tag = COUNT_VIEW_TAG
		}

		super.addView(countTextView)
	}

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)

		val sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
		val sizeHeight = MeasureSpec.getSize(heightMeasureSpec)

		val modeWidth = MeasureSpec.getMode(widthMeasureSpec)
		val modeHeight = MeasureSpec.getMode(heightMeasureSpec)

		var width = 0
		var height = paddingTop + paddingBottom

		var lineWidth = 0
		var lineHeight = 0

		val childCount = childCount

		var isMaxRowCount = false
		var lineCount = 0
		var rowCount = 0

		for (i in 0 until childCount) {
			val child = getChildAt(i)
			val lastChild = i == childCount - 1

			if (child.visibility == GONE) {
				if (lastChild) {
					width = max(width, lineWidth)
					height += lineHeight
				}
				continue
			}

			measureChildWithMargins(child, widthMeasureSpec, lineWidth, heightMeasureSpec, height)

			val params = child.layoutParams as LayoutParams

			var childWidthMode = MeasureSpec.AT_MOST
			var childWidthSize = sizeWidth

			var childHeightMode = MeasureSpec.AT_MOST
			var childHeightSize = sizeHeight

			if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
				childWidthMode = MeasureSpec.EXACTLY
				childWidthSize -= params.leftMargin + params.rightMargin
			} else if (params.width >= 0) {
				childWidthMode = MeasureSpec.EXACTLY
				childWidthSize = params.width
			}

			if (params.height >= 0) {
				childHeightMode = MeasureSpec.EXACTLY
				childHeightSize = params.height
			} else if (modeHeight == MeasureSpec.UNSPECIFIED) {
				childHeightMode = MeasureSpec.UNSPECIFIED
				childHeightSize = 0
			}

			child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode), MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode))

			if (child.measuredWidth >= sizeWidth) {
				childWidthSize = (sizeWidth * 0.8).toInt()
				child.measure(MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode), MeasureSpec.makeMeasureSpec(childHeightSize, childHeightMode))
			}

			if (isMaxRowCount) {
				continue
			}

			val childWidth = child.measuredWidth + params.leftMargin + params.rightMargin

			// 가로 길이를 넘어갔을 경우
			if (lineWidth + childWidth > sizeWidth) {
				width = max(width, lineWidth)
				lineWidth = childWidth

				height += lineHeight
				lineHeight = child.measuredHeight + params.topMargin + params.bottomMargin

				rowCount = 0
				lineCount += 1

				if (maxLineCount != NOT_CONSTRAINT_LINE_COUNT && lineCount == maxLineCount) {
					isMaxRowCount = true
				}

			} else {
				rowCount += 1

				lineWidth += childWidth
				lineHeight = max(lineHeight, child.measuredHeight + params.topMargin + params.bottomMargin)
			}

			if (lastChild) {
				width = max(width, lineWidth)

				if (maxLineCount == NOT_CONSTRAINT_LINE_COUNT || rowCount != 0) {
					height += lineHeight
				}
			}
		}

		width += paddingLeft + paddingRight

		setMeasuredDimension(
			if (modeWidth == MeasureSpec.EXACTLY) sizeWidth else width,
			if (modeHeight == MeasureSpec.EXACTLY) sizeHeight else height)
	}

	@SuppressLint("SetTextI18n")
	override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {

		lineList.clear()
		lineHeightList.clear()
		lineMarginList.clear()

		val width = width
		val height = height

		var linesSum = paddingTop

		var lineWidth = 0
		var lineHeight = 0

		// 첫 줄에 들어갈 lineViewList 생성
		var lineViews: MutableList<View> = ArrayList()

		val horizontalGravityFactor = when (gravity and Gravity.HORIZONTAL_GRAVITY_MASK) {
			Gravity.LEFT -> 0f
			Gravity.CENTER_HORIZONTAL -> .5f
			Gravity.RIGHT -> 1f
			else -> 0f
		}

		var isMaxRowCount = false
		var displayIndex = 0

		for (i in 0 until childCount) {
			val child = getChildAt(i)
			val lastChild = i == childCount - 1
			if (child.visibility == GONE) {
				continue
			}

			if (isMaxRowCount && !lastChild) {
				child.visibility = GONE
				continue
			}

			val params = child.layoutParams as LayoutParams

			val childWidth = child.measuredWidth + params.leftMargin + params.rightMargin
			val childHeight = child.measuredHeight + params.bottomMargin + params.topMargin

			// 가로 길이를 넘어갔을 경우,
			if (lineWidth + childWidth > width) {
				if (maxLineCount != NOT_CONSTRAINT_LINE_COUNT && lineList.size == maxLineCount - 1) {
					isMaxRowCount = true

					if (lastChild) {
						val prevChild = getChildAt(displayIndex - 1)
						prevChild.visibility = GONE

						child.visibility = VISIBLE
					} else {
						child.visibility = GONE
					}

					continue
				}

				lineHeightList.add(lineHeight)
				lineList.add(lineViews)
				lineMarginList.add(((width - lineWidth) * horizontalGravityFactor).toInt() + paddingLeft)
				linesSum += lineHeight

				lineHeight = 0
				lineWidth = 0
				lineViews = ArrayList() // 다음 줄에 들어갈 LineViewList 생성
			}

			// 아이템이 추가 될 때 마다 아이템의 가로 길이를 측정하여 저장
			lineWidth += childWidth
			lineHeight = max(lineHeight, childHeight)
			lineViews.add(child)
			displayIndex += 1
		}

		lineHeightList.add(lineHeight)
		lineList.add(lineViews)
		lineMarginList.add(((width - lineWidth) * horizontalGravityFactor).toInt() + paddingLeft)
		linesSum += lineHeight

		var total = 0
		for (item in lineList) {
			total += item.size
		}

		if (maxLineCount != NOT_CONSTRAINT_LINE_COUNT && lineList.size == maxLineCount && total < childCount) {
			isMaxRowCount = true
		}

		if (maxLineCount != NOT_CONSTRAINT_LINE_COUNT) {
			val lastCountView = getChildAt(childCount - 1)
			if (lastCountView != null) {
				lastCountView.visibility = if (isMaxRowCount) VISIBLE else GONE
				if (isMaxRowCount && isHiddenItemCountView(lastCountView)) {
					(lastCountView as TextView).text = "+" + (childCount - displayIndex)
				}
			}
		}

		var verticalGravityMargin = 0
		when (gravity and Gravity.VERTICAL_GRAVITY_MASK) {
			Gravity.TOP -> {}
			Gravity.CENTER_VERTICAL -> verticalGravityMargin = (height - linesSum) / 2
			Gravity.BOTTOM -> verticalGravityMargin = height - linesSum
			else -> {}
		}

		var numLines = lineList.size

		// 여기서 카운트 view에 대한 조건 처리가 필요
		if (maxLineCount != NOT_CONSTRAINT_LINE_COUNT && numLines >= maxLineCount - 1) { // todo 마지막 조건 삭제해야할까?
			val lastDisplayedLine: List<View> = lineList[lineList.size - 1]
			if (lastDisplayedLine.size == 1 && isHiddenItemCountView(lastDisplayedLine[0])) {
				numLines -= 1
			}
		}

		var left: Int
		var top = paddingTop

		for (i in 0 until numLines) {
			lineHeight = lineHeightList[i]
			lineViews = lineList[i]
			left = lineMarginList[i]

			val children = lineViews.size

			for (j in 0 until children) {
				val child = lineViews[j]
				if (child.visibility == GONE) {
					continue
				}

				val lp = child.layoutParams as LayoutParams

				// if height is match_parent we need to remeasure child to line height
				if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
					var childWidthMode = MeasureSpec.AT_MOST
					var childWidthSize = lineWidth

					if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
						childWidthMode = MeasureSpec.EXACTLY
					} else if (lp.width >= 0) {
						childWidthMode = MeasureSpec.EXACTLY
						childWidthSize = lp.width
					}

					child.measure(
						MeasureSpec.makeMeasureSpec(childWidthSize, childWidthMode),
						MeasureSpec.makeMeasureSpec(lineHeight - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY)
					)
				}

				val childWidth = child.measuredWidth
				val childHeight = child.measuredHeight

				var gravityMargin = 0

				if (Gravity.isVertical(lp.gravity)) {
					when (lp.gravity) {
						Gravity.TOP -> {}
						Gravity.CENTER_VERTICAL, Gravity.CENTER -> gravityMargin = (lineHeight - childHeight - lp.topMargin - lp.bottomMargin) / 2
						Gravity.BOTTOM -> gravityMargin = lineHeight - childHeight - lp.topMargin - lp.bottomMargin
						else -> {}
					}
				}

				child.layout(
					left + lp.leftMargin,
					top + lp.topMargin + gravityMargin + verticalGravityMargin,
					left + childWidth + lp.leftMargin,
					top + childHeight + lp.topMargin + gravityMargin + verticalGravityMargin)

				left += childWidth + lp.leftMargin + lp.rightMargin
			}
			top += lineHeight
		}
	}

	private fun isHiddenItemCountView(child: View) = if (child.tag !is String) {
		false
	} else {
		COUNT_VIEW_TAG == (child.tag as String)
	}

	override fun generateLayoutParams(p: ViewGroup.LayoutParams): LayoutParams {
		return LayoutParams(p)
	}

	override fun generateLayoutParams(attrs: AttributeSet): LayoutParams {
		return LayoutParams(context, attrs)
	}

	override fun generateDefaultLayoutParams(): LayoutParams {
		return LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
	}

	override fun addView(child: View) {
		if (maxLineCount == NOT_CONSTRAINT_LINE_COUNT) {
			super.addView(child)
		} else {
			// add the child at front of hidden item count view
			addView(child, childCount - 1)
		}
	}

	override fun addView(child: View, width: Int, height: Int) {
		if (maxLineCount == NOT_CONSTRAINT_LINE_COUNT) {
			super.addView(child, width, height)
		} else {
			val params = generateDefaultLayoutParams()
			params.width = width
			params.height = height
			// add the child at front of hidden item count view
			addView(child, childCount - 1, params)
		}
	}

	override fun addView(child: View, params: ViewGroup.LayoutParams) {
		if (maxLineCount == NOT_CONSTRAINT_LINE_COUNT) {
			super.addView(child, params)
		} else {
			// add the child at front of hidden item count view
			addView(child, childCount - 1, params)
		}
	}

	override fun removeAllViews() {
		super.removeAllViews()
		if (maxLineCount != NOT_CONSTRAINT_LINE_COUNT) {
			addHiddenItemCountView()
		}
	}

	class LayoutParams : MarginLayoutParams {
		var gravity = -1

		constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
			context.withStyledAttributes(attrs, R.styleable.FlowLayout_Layout) {
				gravity = getInt(R.styleable.FlowLayout_Layout_android_layout_gravity, -1)
			}
		}

		constructor(width: Int, height: Int) : super(width, height)
		constructor(source: ViewGroup.LayoutParams?) : super(source)
	}
}