package org.qiah.balabala.util

import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import org.qiah.balabala.BaseApplication
import org.qiah.balabala.bean.Nikke

fun getHeight(): Int = BaseApplication.context().resources.displayMetrics.heightPixels
fun getWidth(): Int = BaseApplication.context().resources.displayMetrics.widthPixels

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
// 触摸区域扩大
fun View.increaseTouchRange(range: Int = 10) {
    val scale = context.resources.displayMetrics.density
    val result = (range * scale + 0.5f).toInt()
    isEnabled = true
    if (this.parent is ViewGroup) {
        val group = this.parent as ViewGroup
        group.post {
            val rect = Rect()
            this.getHitRect(rect)
            rect.top -= result // increase top hit area
            rect.left -= result // increase left hit area
            rect.bottom += result // increase bottom hit area
            rect.right += result // increase right hit area
            group.touchDelegate = TouchDelegate(rect, this)
        }
    }
}
inline fun <T : View> T.singleClick(
    time: Long = 500,
    increase: Boolean = false,
    range: Int = 10,
    crossinline block: (T) -> Unit
) {
    if (increase) {
        this.increaseTouchRange(range)
    }
    setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()
        val interval = currentTimeMillis - lastClickTime
        if (interval > time || interval < 0) {
            // 小于0是为了规避用户往前调手机时间
            lastClickTime = currentTimeMillis
            block(this)
        }
    }
}
var <T : View> T.lastClickTime: Long
    set(value) = setTag(Int.MAX_VALUE, value)
    get() = getTag(Int.MAX_VALUE) as? Long ?: 0

/**
 * 尺寸相关
 */
inline fun <reified T> Float.dp(): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        BaseApplication.context().resources.displayMetrics
    )
    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

inline fun <reified T> Int.dp(): T {
    return this.toFloat().dp()
}

inline fun <reified T> Float.sp(): T {
    val result = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this,
        BaseApplication.context().resources.displayMetrics
    )
    return when (T::class) {
        Float::class -> result as T
        Int::class -> result.toInt() as T
        else -> throw IllegalStateException("Type not supported")
    }
}

inline fun <reified T> Int.sp(): T {
    return this.toFloat().sp()
}

/**
 * @param drawableResId Drawable路径id
 * @param radius 圆角弧度
 */
fun ImageView.load(drawableResId: Drawable, radius: Int) {
    Glide.with(this)
        .load(drawableResId)
        .apply(roundedCorner(radius))
        .into(this)
}
fun ImageView.load(url: String?, radius: Int) {
    url?.let {
        Glide.with(this)
            .load(it)
            .apply(roundedCorner(radius))
            .into(this)
    }
}
fun ImageView.load(@DrawableRes drawableResId: Int, radius: Int) {
    BaseApplication.context().getDrawable(drawableResId)?.let {
        Glide.with(this)
            .load(it)
            .apply(roundedCorner(radius))
            .into(this)
    }
}

/**
 * @param url 图片链接
 * @param isCircle 是否为圆
 */
fun ImageView.load(url: String?, isCircle: Boolean = false) {
    val a = Glide.with(this)
        .load(url)
        .centerCrop()
    if (isCircle) {
        a.circleCrop()
            .into(this)
    } else {
        a.into(this)
    }
}
fun ImageView.load(@DrawableRes drawableResId: Int, isCircle: Boolean = false) {
    BaseApplication.context().getDrawable(drawableResId)?.let {
        val a = Glide.with(this)
            .load(it)
            .centerCrop()
        if (isCircle) {
            a.circleCrop()
                .into(this)
        } else {
            a.into(this)
        }
    }
}
fun ImageView.load(nikke: Nikke, b: Boolean) {
    nikke.avatarId.nullOrNot(
        {
            this.load(nikke.avatarPath, b)
        },
        {
            this.load(nikke.avatarId!!, b)
        }
    )
}
fun ImageView.load(nikke: Nikke, r: Int) {
    nikke.avatarId.nullOrNot(
        {
            this.load(nikke.avatarPath, r)
        },
        {
            this.load(nikke.avatarId!!, r)
        }
    )
}

private val centerCrop: CenterCrop by lazy { CenterCrop() }
private val roundedCornersMap: MutableMap<Int, RequestOptions> = mutableMapOf()
private fun roundedCorner(radius: Int): RequestOptions {
    return roundedCornersMap.getOrPut(radius) {
        RequestOptions().transform(centerCrop, RoundedCorners(radius))
    }
}
inline fun <T> T?.nullOrNot(ifNull: () -> Unit, notNull: (T) -> Unit) {
    this?.apply {
        notNull(this)
        return
    }
    ifNull()
}
fun View?.show() {
    if (this?.visibility != View.VISIBLE) {
        this?.visibility = View.VISIBLE
    }
}

fun View?.hide() {
    if (this?.visibility != View.INVISIBLE) {
        this?.visibility = View.INVISIBLE
    }
}

fun View?.gone() {
    if (this?.visibility != View.GONE) {
        this?.visibility = View.GONE
    }
}
