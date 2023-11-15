package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType

data class ChatRightText(
    val text: String
) : MyType() {
    override fun viewType(): Int = CHAT_RIGHT_TEXT
}
