package org.jxxy.debug.sensors

import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

fun View.getTargetViews(
    event: MotionEvent,
    list: ArrayList<View>,
) {
    if (this.visibility == View.VISIBLE && this.containEvent(event)) {
        if (this is ViewGroup) {
            list.add(this)
            (this as ViewGroup).children.forEach {
                it.getTargetViews(event, list)
            }
        } else {
            if (this.isClickable) {
                list.add(this)
            }
        }
    }
}

fun View.containEvent(event: MotionEvent): Boolean {
    val i = IntArray(2)
    this.getLocationOnScreen(i)
    return i[0] < event.x.toInt() && i[0] + width > event.x.toInt() && i[1] < event.y.toInt() && i[1] + height > event.y.toInt()
}
