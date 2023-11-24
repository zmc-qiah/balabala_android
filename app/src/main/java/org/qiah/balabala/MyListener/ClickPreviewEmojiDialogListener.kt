package org.qiah.balabala.MyListener

import org.qiah.balabala.util.MyType

interface ClickPreviewEmojiDialogListener {
    fun onClockMoveTop(entry: MyType)
    fun onClockDelete(entry: MyType)
    fun onClock(entry: MyType)
}
