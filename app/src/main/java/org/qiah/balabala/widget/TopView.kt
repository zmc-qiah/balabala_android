package org.qiah.balabala.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View
import org.qiah.balabala.R
import org.qiah.balabala.util.ResourceUtil

class TopView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private val TAG = TopView::class.simpleName
    private val paint: Paint
    private val point = 8
    private val space = 5
    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ResourceUtil.getColor(R.color.top_point)
            alpha = 100
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val c1 = width / (point + space) + 1
            val c2 = height / (point + space) + 1
            for (i in 1 until c2) {
                for (j in 1 until c1) {
                    Log.d(TAG, "onDraw: ${space * j + point / 2 + point * (j - 1)} ${(space * i + point / 2 + point * (i - 1)).toFloat()}")
                    it.drawCircle((space * j + point / 2 + point * (j - 1)).toFloat(), (space * i + point / 2 + point * (i - 1)).toFloat(), point / 2f, paint)
                }
            }
        }
    }
}
