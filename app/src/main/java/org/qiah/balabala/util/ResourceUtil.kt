package org.qiah.balabala.util

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import org.qiah.balabala.BaseApplication

/**
 * @author : Wangjf
 * @date : 2021/1/19
 */
object ResourceUtil {
    fun getString(@StringRes resId: Int): String {
        return BaseApplication.context().getString(resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return BaseApplication.context().getString(resId, *formatArgs)
    }

    @ColorInt
    fun getColor(@ColorRes colorResId: Int): Int {
        return ContextCompat.getColor(
            BaseApplication.context(),
            colorResId
        )
    }

    fun getDrawable(@DrawableRes drawableResId: Int): Drawable? {
        return ContextCompat.getDrawable(
            BaseApplication.context(),
            drawableResId
        )
    }
}
