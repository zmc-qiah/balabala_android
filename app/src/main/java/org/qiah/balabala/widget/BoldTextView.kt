package org.qiah.balabala.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import org.qiah.balabala.util.IconFontManager

class BoldTextView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : AppCompatTextView(
    context,
    attributeSet,
    defStyleAttr
) {

    init {
        typeface = IconFontManager.getBoldAsset()
    }
}
