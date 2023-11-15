package org.qiah.balabala.bean

import org.qiah.balabala.util.MyType

data class ChatRightImage(
    val path: String
) : MyType() {
    override fun viewType(): Int = CHAT_RIGHT_IMAGE
}
