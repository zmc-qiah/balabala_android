package org.qiah.balabala.util

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class NormalTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    )

    init {
        typeface = IconFontManager.getNormalAsset()
    }
}
