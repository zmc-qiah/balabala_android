package org.qiah.balabala.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import org.qiah.balabala.R
import org.qiah.balabala.util.ResourceUtil

class NikkeAvatarIV @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attributeSet, defStyleAttr) {
    private val TAG = NikkeAvatarIV::class.simpleName
    private val paint2: Paint

    init {

        paint2 = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = ResourceUtil.getColor(R.color.grey_1)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
//            val c1 = width / (point + space) + 1
//            val c2 = height / (point + space) + 1
//            for (i in 1 until c2) {
//                for (j in 1 until c1) {
//                    Log.d(TAG, "onDraw: ${space * j + point / 2 + point * (j - 1)} ${(space * i + point / 2 + point * (i - 1)).toFloat()}")
//                    it.drawCircle((space * j + point / 2 + point * (j - 1)).toFloat(), (space * i + point / 2 + point * (i - 1)).toFloat(), point / 2f, paint1)
//                }
//            }
            val x1 = width
            val y1 = 0
            val y2 = height
            val x2 = width
            val y3 = height
            val x3 = (width * 16 / 20.0).toFloat()
            val path = Path()
            path.moveTo(x1.toFloat(), y1.toFloat())
            path.lineTo(x2.toFloat(), y2.toFloat())
            path.lineTo(x3, y3.toFloat())
            path.close()

            canvas.drawPath(path, paint2)
        }
    }
}
