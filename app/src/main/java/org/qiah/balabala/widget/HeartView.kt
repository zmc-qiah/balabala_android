package org.qiah.balabala.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class HeartView : View {
    private var mMeasureWidth = 0
    private var mWidthMode = 0
    private var mMeasureHeight = 0
    private var mHeightMode = 0
    private var paint: Paint? = null

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        paint = Paint() // 实例画笔
        paint!!.isAntiAlias = true // 抗锯齿
        paint!!.strokeWidth = 2f // 画笔宽度
        paint!!.color = Color.RED // 画笔颜色
        paint!!.style = Paint.Style.FILL // 画笔样式
    }

    /**
     * 测量
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec)
        mMeasureWidth = MeasureSpec.getSize(widthMeasureSpec)
        mMeasureHeight = MeasureSpec.getSize(heightMeasureSpec)
        if (mWidthMode == MeasureSpec.AT_MOST && mHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, 200)
        } else if (mWidthMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(200, mMeasureHeight)
        } else if (mHeightMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mMeasureWidth, 200)
        } else {
            setMeasuredDimension(mMeasureWidth, mMeasureHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width // 获取屏幕宽
        val height = height // 获取屏幕高

        /**
         * 绘制心形
         */
        // 左半面
        val path = Path()
        path.moveTo((width / 2).toFloat(), (height / 4).toFloat())
        path.cubicTo(
            (width * 6 / 7).toFloat(),
            (height / 9).toFloat(),
            (width * 12 / 13).toFloat(),
            (height * 2 / 5).toFloat(),
            (width / 2).toFloat(),
            (height * 7 / 12).toFloat()
        )
        canvas.drawPath(path, paint!!)
        // 右半面
        val path2 = Path()
        path2.moveTo((width / 2).toFloat(), (height / 4).toFloat())
        path2.cubicTo(
            (width / 7).toFloat(),
            (height / 9).toFloat(),
            (width / 13).toFloat(),
            (height * 2 / 5).toFloat(),
            (width / 2).toFloat(),
            (height * 7 / 12).toFloat()
        )
        canvas.drawPath(path2, paint!!)
    }
}