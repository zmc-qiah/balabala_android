package org.qiah.balabala.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import org.qiah.balabala.R
import org.qiah.balabala.util.ResourceUtil
import org.qiah.balabala.util.dp

class ChatRecycleView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RecyclerView(context, attributeSet, defStyleAttr) {
    private val TAG = ChatRecycleView::class.simpleName
    private val paint: Paint
    private val cnt = 32
    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ResourceUtil.getColor(R.color.chat_rv_line)
            strokeWidth = 0.7f.dp()
            alpha = 75
        }
    }
    override fun onDraw(c: Canvas?) {
        val a = IntArray(2)
        getLocationOnScreen(a)
        val v = arrayOf(width, height)
        super.onDraw(c)
        c?.let {
            Log.d(TAG, "onDraw: ${a[0]}, ${a[1]}, ${a[0] + width}, ${a[1] + height}")
            val w = width / cnt
            val row = cnt
            val col = height / w
            for (i in 1 until row + 1) {
                c.drawLine(w * i, 0, w * i, height, paint)
            }
            for (i in 1 until col + 1) {
                c.drawLine(0, w * i, width, w * i, paint)
            }
        }
    }
}

private fun Canvas.drawLine(i: Int, i1: Int, width: Int, height: Int, paint: Paint) {
    this.drawLine(i.toFloat(), i1.toFloat(), width.toFloat(), height.toFloat(), paint)
}
