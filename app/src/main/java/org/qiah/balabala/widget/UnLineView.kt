package org.qiah.balabala.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import org.qiah.balabala.R
import org.qiah.balabala.util.ResourceUtil

class UnLineView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attributeSet, defStyleAttr) {
    private val paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ResourceUtil.getColor(R.color.common_text_white)
            style = Paint.Style.FILL
        }
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val path = Path()
            path.moveTo(0f, height.toFloat())
            path.lineTo((width * 0.06).toFloat(), 0f)
            path.lineTo((width * 0.94).toFloat(), 0f)
            path.lineTo(width.toFloat(), height.toFloat())
            path.close()
            it.drawPath(path, paint)
        }
    }
}
