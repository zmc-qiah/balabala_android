package org.qiah.balabala.util

import android.content.Context
import android.graphics.* // ktlint-disable no-wildcard-imports
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import org.qiah.balabala.R

class RoundHelper {
    private var mContext: Context? = null
    private var mView: View? = null
    private var mPaint: Paint? = null
    private var mRectF: RectF? = null
    private var mStrokeRectF: RectF? = null
    private var mPath: Path? = null
    private var mTempPath: Path? = null
    private var mXfermode: Xfermode? = null
    private var isCircle = false
    private var mRadii = FloatArray(8)
    private var mStrokeRadii = FloatArray(8)
    private var mWidth = 0
    private var mHeight = 0
    var mStrokeColor = 0
    var mStrokeWidth = 0f
    var mRadiusTopLeft = 0f
    var mRadiusTopRight = 0f
    var mRadiusBottomLeft = 0f
    var mRadiusBottomRight = 0f

    fun init(
        context: Context,
        attrs: AttributeSet?,
        view: View
    ) {
        if (view is ViewGroup && view.getBackground() == null) {
            view.setBackgroundColor(Color.parseColor("#00000000"))
        }
        mContext = context
        mView = view
        mPaint = Paint()
        mRectF = RectF()
        mStrokeRectF = RectF()
        mPath = Path()
        mTempPath = Path()
        mXfermode =
            PorterDuffXfermode(
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PorterDuff.Mode.DST_OUT
                } else PorterDuff.Mode.DST_IN
            )
        mStrokeColor = Color.TRANSPARENT
        val array = context.obtainStyledAttributes(attrs, R.styleable.RoundHelper) ?: return
        val radius = array.getDimension(R.styleable.RoundHelper_round_corner, 0f)
        val radiusLeft =
            array.getDimension(R.styleable.RoundHelper_round_corner_left, radius)
        val radiusRight =
            array.getDimension(R.styleable.RoundHelper_round_corner_right, radius)
        val radiusTop = array.getDimension(R.styleable.RoundHelper_round_corner_top, radius)
        val radiusBottom =
            array.getDimension(R.styleable.RoundHelper_round_corner_bottom, radius)
        mRadiusTopLeft = array.getDimension(
            R.styleable.RoundHelper_round_corner_top_left,
            if (radiusTop > 0) radiusTop else radiusLeft
        )
        mRadiusTopRight = array.getDimension(
            R.styleable.RoundHelper_round_corner_top_right,
            if (radiusTop > 0) radiusTop else radiusRight
        )
        mRadiusBottomLeft = array.getDimension(
            R.styleable.RoundHelper_round_corner_bottom_left,
            if (radiusBottom > 0) radiusBottom else radiusLeft
        )
        mRadiusBottomRight = array.getDimension(
            R.styleable.RoundHelper_round_corner_bottom_right,
            if (radiusBottom > 0) radiusBottom else radiusRight
        )
        mStrokeWidth = array.getDimension(R.styleable.RoundHelper_round_stroke_width, 0f)
        mStrokeColor = array.getColor(R.styleable.RoundHelper_round_stroke_color, mStrokeColor)
        isCircle = array.getBoolean(R.styleable.RoundHelper_round_as_circle, false)
        array.recycle()
        if (!isCircle) {
            setRadius()
        }
    }

    private fun setRadius() {
        mRadii[1] = mRadiusTopLeft - mStrokeWidth
        mRadii[0] = mRadii[1]
        mRadii[3] = mRadiusTopRight - mStrokeWidth
        mRadii[2] = mRadii[3]
        mRadii[5] = mRadiusBottomRight - mStrokeWidth
        mRadii[4] = mRadii[5]
        mRadii[7] = mRadiusBottomLeft - mStrokeWidth
        mRadii[6] = mRadii[7]
        mStrokeRadii[1] = mRadiusTopLeft
        mStrokeRadii[0] = mStrokeRadii[1]
        mStrokeRadii[3] = mRadiusTopRight
        mStrokeRadii[2] = mStrokeRadii[3]
        mStrokeRadii[5] = mRadiusBottomRight
        mStrokeRadii[4] = mStrokeRadii[5]
        mStrokeRadii[7] = mRadiusBottomLeft
        mStrokeRadii[6] = mStrokeRadii[7]
    }

    fun onSizeChanged(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        if (isCircle) {
            val radius = height.coerceAtMost(width) * 1f / 2 - mStrokeWidth
            mRadiusTopLeft = radius
            mRadiusTopRight = radius
            mRadiusBottomRight = radius
            mRadiusBottomLeft = radius
            setRadius()
        }
        mRectF?.let {
            it[0f, 0f, width.toFloat()] = height.toFloat()
        }
        mStrokeRectF?.let {
            it[mStrokeWidth / 2, mStrokeWidth / 2, width - mStrokeWidth / 2] =
                height - mStrokeWidth / 2
        }
    }

    fun preDraw(canvas: Canvas) {
        canvas.saveLayer(mRectF, null, Canvas.ALL_SAVE_FLAG)
    }

    fun drawPath(canvas: Canvas) {
        mPaint?.reset()
        mPath?.reset()
        mPaint?.isAntiAlias = true
        mPaint?.style = Paint.Style.FILL
        mPaint?.xfermode = mXfermode
        mRectF?.let { mPath?.addRoundRect(it, mRadii, Path.Direction.CCW) }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mTempPath?.reset()
            mRectF?.let { mTempPath?.addRect(it, Path.Direction.CCW) }
            mPath?.let { mTempPath?.op(it, Path.Op.DIFFERENCE) }
            if (mTempPath != null && mPaint != null) {
                canvas.drawPath(mTempPath!!, mPaint!!)
            }
        } else {
            if (mPath != null && mPaint != null) {
                canvas.drawPath(mPath!!, mPaint!!)
            }
        }
        mPaint?.xfermode = null
        canvas.restore()

        // draw stroke
        if (mStrokeWidth > 0) {
            mPaint?.style = Paint.Style.STROKE
            mPaint?.strokeWidth = mStrokeWidth
            mPaint?.color = mStrokeColor
            mPath?.reset()
            mStrokeRectF?.let { mPath?.addRoundRect(it, mStrokeRadii, Path.Direction.CCW) }
            if (mPath != null && mPaint != null) {
                canvas.drawPath(mPath!!, mPaint!!)
            }
            val sx = (mWidth - 2 * mStrokeWidth) / mWidth
            val sy = (mHeight - 2 * mStrokeWidth) / mHeight
            // 缩小画布，使图片内容不被borders覆盖
            canvas.scale(sx, sy, mWidth / 2.0f, mHeight / 2.0f)
        }
    }

    fun setCircle(isCircle: Boolean) {
        this.isCircle = isCircle
    }

    fun setRadius(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusTopLeft = radius
        mRadiusTopRight = radius
        mRadiusBottomLeft = radius
        mRadiusBottomRight = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadius(
        radiusTopLeftDp: Float,
        radiusTopRightDp: Float,
        radiusBottomLeftDp: Float,
        radiusBottomRightDp: Float
    ) {
        if (mContext == null) {
            return
        }
        mRadiusTopLeft = radiusTopLeftDp
        mRadiusTopRight = radiusTopRightDp
        mRadiusBottomLeft = radiusBottomLeftDp
        mRadiusBottomRight = radiusBottomRightDp
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusLeft(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusTopLeft = radius
        mRadiusBottomLeft = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusRight(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusTopRight = radius
        mRadiusBottomRight = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusTop(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusTopLeft = radius
        mRadiusTopRight = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusBottom(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusBottomLeft = radius
        mRadiusBottomRight = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusTopLeft(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusTopLeft = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusTopRight(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusTopRight = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusBottomLeft(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusBottomLeft = radius
        setRadius()
        mView?.invalidate()
    }

    fun setRadiusBottomRight(radius: Float) {
        if (mContext == null) {
            return
        }
        mRadiusBottomRight = radius
        setRadius()
        mView?.invalidate()
    }

    fun setStrokeWidth(widthDp: Float) {
        if (mContext == null) {
            return
        }
        mStrokeWidth = widthDp
        if (mView != null) {
            setRadius()
            onSizeChanged(mWidth, mHeight)
            mView?.invalidate()
        }
    }

    fun setStrokeColor(color: Int) {
        mStrokeColor = color
        mView?.invalidate()
    }

    fun setStrokeWidthColor(widthDp: Float, color: Int) {
        if (mContext == null) {
            return
        }
        mStrokeWidth = widthDp
        mStrokeColor = color
        mView?.let {
            setRadius()
            onSizeChanged(mWidth, mHeight)
            it.invalidate()
        }
    }
}
