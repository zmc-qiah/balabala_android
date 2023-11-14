package org.qiah.balabala.bean

import android.graphics.drawable.Drawable
import org.qiah.balabala.util.MyType

data class MainNikke(
    val drawable: Drawable,
    val name: String,
    val message: String
) : MyType() {
    override fun viewType(): Int = MyType.MAIN_NIKKE
}
